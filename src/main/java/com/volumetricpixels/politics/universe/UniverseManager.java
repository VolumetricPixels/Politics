/*
 * This file is part of Politics.
 *
 * Copyright (c) 2012-2012, VolumetricPixels <http://volumetricpixels.com/>
 * Politics is licensed under the Affero General Public License Version 3.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.volumetricpixels.politics.universe;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.spout.api.exception.ConfigurationException;
import org.spout.api.geo.World;
import org.spout.api.util.config.yaml.YamlConfiguration;

import org.apache.commons.io.FileUtils;

import org.bson.BSONDecoder;
import org.bson.BSONEncoder;
import org.bson.BSONObject;
import org.bson.BasicBSONDecoder;
import org.bson.BasicBSONEncoder;

import com.volumetricpixels.politics.Politics;
import com.volumetricpixels.politics.PoliticsPlugin;
import com.volumetricpixels.politics.exception.InvalidConfigurationException;
import com.volumetricpixels.politics.group.Group;
import com.volumetricpixels.politics.group.GroupProperty;
import com.volumetricpixels.politics.group.level.GroupLevel;
import com.volumetricpixels.politics.world.PoliticsWorld;

/**
 * Contains all universes
 */
public class UniverseManager {
    /**
     * The rules of the universe.
     */
    private Map<String, UniverseRules> rules;

    /**
     * Universes mapped to their names.
     */
    private Map<String, Universe> universes;

    /**
     * Stores groups.
     */
    private TIntObjectMap<Group> groups;

    /**
     * Worlds mapped to their levels.
     */
    private Map<PoliticsWorld, Map<GroupLevel, Universe>> worldLevels;

    /**
     * The next id to assign a Group.
     */
    private int nextId = 0xffffffff;

    /**
     * C'tor
     */
    public UniverseManager() {
    }

    /**
     * Loads the rules into memory.
     */
    public void loadRules() {
        Politics.getFileSystem().getRulesDir().mkdirs();
        rules = new HashMap<String, UniverseRules>();
        for (final File file : Politics.getFileSystem().getRulesDir().listFiles()) {
            final String fileName = file.getName();
            if (!fileName.endsWith(".yml") || fileName.length() <= 4) {
                continue;
            }
            final String name = fileName.substring(0, fileName.length() - 4);

            final YamlConfiguration configFile = new YamlConfiguration(file);
            try {
                configFile.load();
            } catch (final ConfigurationException ex) {
                new InvalidConfigurationException("Invalid universe YAML file `" + fileName + "'!", ex).printStackTrace();
                continue;
            }

            final UniverseRules thisRules = UniverseRules.load(name, configFile);
            final String ruleName = thisRules.getName();
            rules.put(ruleName.toLowerCase(), thisRules);
        }
    }

    /**
     * Loads all universes into memory from files.
     */
    public void loadUniverses() {
        final BSONDecoder decoder = new BasicBSONDecoder();
        universes = new HashMap<String, Universe>();
        groups = new TIntObjectHashMap<Group>();
        Politics.getFileSystem().getUniversesDir().mkdirs();
        for (final File file : Politics.getFileSystem().getUniversesDir().listFiles()) {
            final String fileName = file.getName();
            if (!fileName.endsWith(".ptu") || fileName.length() <= 4) {
                continue;
            }

            byte[] data;
            try {
                data = FileUtils.readFileToByteArray(file);
            } catch (final IOException ex) {
                new InvalidConfigurationException("Could not read universe file `" + fileName + "'!", ex).printStackTrace();
                continue;
            }

            final BSONObject object = decoder.readObject(data);

            final Universe universe = Universe.fromBSONObject(object);
            universes.put(universe.getName(), universe);

            for (final Group group : universe.getGroups()) {
                if (groups.put(group.getUid(), group) != null) {
                    PoliticsPlugin.logger().log(Level.WARNING, "Duplicate group id " + group.getUid() + "!");
                }
                if (group.getUid() > nextId) {
                    nextId = group.getUid();
                }
            }
        }

        // Populate World levels
        worldLevels = new HashMap<PoliticsWorld, Map<GroupLevel, Universe>>();
        for (final Universe universe : universes.values()) {
            for (final GroupLevel level : universe.getRules().getGroupLevels()) {
                for (final PoliticsWorld world : universe.getWorlds()) {
                    Map<GroupLevel, Universe> levelMap = worldLevels.get(world);
                    if (levelMap == null) {
                        levelMap = new HashMap<GroupLevel, Universe>();
                        worldLevels.put(world, levelMap);
                    }
                    final Universe prev = levelMap.put(level, universe);
                    if (prev != null) {
                        new InvalidConfigurationException("Multiple universes are conflicting on the same world! Universe name: "
                                + universe.getName() + "; Rules name: " + universe.getRules().getName()).printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Saves all universes in memory to files.
     */
    public void saveUniverses() {
        final BSONEncoder encoder = new BasicBSONEncoder();
        Politics.getFileSystem().getUniversesDir().mkdirs();
        for (final Universe universe : universes.values()) {
            if (!universe.canStore()) continue;
            final String fileName = universe.getName() + ".cou";
            final File universeFile = new File(Politics.getFileSystem().getUniversesDir(), fileName);

            final byte[] data = encoder.encode(universe.toBSONObject());
            try {
                FileUtils.writeByteArrayToFile(universeFile, data);
            } catch (final IOException ex) {
                PoliticsPlugin.logger().log(Level.SEVERE, "Could not save universe file `" + fileName + "' due to error!", ex);
                continue;
            }
            // TODO make backups
        }
    }

    /**
     * Gets a universe by its name.
     * 
     * @param name
     * @return
     */
    public Universe getUniverse(final String name) {
        return universes.get(name.toLowerCase());
    }

    /**
     * Gets the rules with the corresponding name.
     * 
     * @param rulesName
     * @return
     */
    public UniverseRules getRules(final String rulesName) {
        return rules.get(rulesName);
    }

    /**
     * Returns a list of all UniverseRules.
     * 
     * @return
     */
    public List<UniverseRules> listRules() {
        return new ArrayList<UniverseRules>(rules.values());
    }

    /**
     * Gets a universe from its world and group level.
     * 
     * @param world
     * @param level
     * @return
     */
    public Universe getUniverse(final World world, final GroupLevel level) {
        final PoliticsWorld cw = Politics.getWorld(world);
        if (cw == null) {
            return null;
        }
        return getUniverse(cw, level);
    }

    /**
     * Gets a list of all GroupLevels.
     * 
     * @return
     */
    public List<GroupLevel> getGroupLevels() {
        final List<GroupLevel> ret = new ArrayList<GroupLevel>();
        for (final UniverseRules rules : this.rules.values()) {
            ret.addAll(rules.getGroupLevels());
        }
        return ret;
    }

    /**
     * Gets a group by its id.
     * 
     * @param id
     * @return
     */
    public Group getGroupById(final int id) {
        return groups.get(id);
    }

    /**
     * Gets a group by their tag.
     * 
     * @param tag
     * @return
     */
    public Group getGroupByTag(final String tag) {
        for (final Group g : groups.valueCollection()) {
            if (g.getStringProperty(GroupProperty.TAG).equalsIgnoreCase(tag)) {
                return g;
            }
        }
        return null;
    }

    /**
     * Gets the universe of the given CommandSource.
     * 
     * @param world
     * @param level
     * @return
     */
    public Universe getUniverse(final PoliticsWorld world, final GroupLevel level) {
        final Map<GroupLevel, Universe> levelUniverses = worldLevels.get(world);
        if (levelUniverses == null) {
            return null;
        }
        return levelUniverses.get(world);
    }

    /**
     * Gets the group levels in the given world.
     * 
     * @param world
     * @return
     */
    public List<GroupLevel> getLevelsOfWorld(final PoliticsWorld world) {
        final Map<GroupLevel, Universe> levelUniverses = worldLevels.get(world);
        if (levelUniverses == null) {
            return new ArrayList<GroupLevel>();
        }
        return new ArrayList<GroupLevel>(levelUniverses.keySet());
    }

    /**
     * Creates a new universe with the given name.
     * 
     * @param name
     * @param theRules
     * @return the created universe
     */
    public Universe createUniverse(final String name, final UniverseRules theRules) {
        final Universe universe = new Universe(name, theRules);
        universes.put(name, universe);
        return universe;
    }

    /**
     * Destroys the given universe.
     * 
     * @param universe
     */
    public void destroyUniverse(final Universe universe) {
        universes.remove(universe.getName());
        for (final Group group : universe.getGroups()) {
            universe.destroyGroup(group);
        }
    }

    /**
     * Gets the next ID to use for a group.
     * 
     * @return
     */
    public int nextId() {
        while (getGroupById(nextId) != null) {
            nextId++;
        }
        return nextId;
    }
}
