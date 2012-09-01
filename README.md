Politics
===========

![Brock Obama](http://i.imgur.com/3jv8G.jpg)

Politics is a sub-community management plugin for [Spout](http://spout.org).

License
-------
Politics is licensed under the [Affero General Public License Version 3][License]. Please see the `LICENSE.txt` file for details.

Compiling
---------
Politics uses Maven to handle its dependencies.

* Install [Maven 2 or 3](http://maven.apache.org/download.html)
* Checkout this repo and run: `mvn`

Coding Standards
----------------------------------
* If / for / while / switch statement: if (conditions && stuff) {
* Method: public void method(Object paramObject) {
* No Tabs, Spaces only!
* No trailing whitespace
* Mid-statement newlines at a 200 column limit
* camelCase, no under_scores except constants
* Constants in full caps with underscores
* Keep the same formatting style everywhere

Pull Request Standards
----------------------------------
* Sign-off on all commits!
* Finished product must compile and run!
* No merges should be included in pull requests unless the pull request's purpose is a merge.
* Number of commits in a pull request should be kept to *one commit* and all additional commits must be *squashed*. Pull requests that make multiple changes should have one commit per change.
* Pull requests must include any applicable license headers. (These are generated when running `mvn`)


**Please follow the above conventions if you want your pull request(s) accepted.**

[License]: http://www.gnu.org/licenses/agpl.html

