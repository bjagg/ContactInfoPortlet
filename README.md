# Contact Info Portlet

## Table of Contects
  - [Description](#desc)
  - [Installation](#install)
  - [Configuration](#config)
  - [Where to Get Help](#help)
  - [Contribution Guidelines](#contrib)
  - [License](#license)

## <a name="desc"></a> Description

This project encompasses two portlets oriented toward two types of users:
students and employees. The portlets will prompt the user to update their
current contact information. The initial user data comes from a JSON feed
but will then be captured in a local database. The database will take
precedence. The differences between the two are the time periods for prompting,
the information prompted and and source of data. In particular, the student
portlet prompts at the start of each semester, whereas the employee portlet
prompts every 90 days.

## <a name="install"></a> Installation

This project is currently very specific for Sinclair College, thus is not
included in uPortal or Maven Central. Installation is a manual process.

### Set Up External Properties

This process matches the same effort for uPortal 5. If you have configured
uPortal 5, only the last step is should be followed, if needed.

  1. Create a directory for external properties. This is usually `portal`
     under the Tomcat Home directory.
  2. Create a `global.properties` file in the directory. This file should
     contain values shared by uPortal and other portlets, such as database
     information.
  3. Configure `portal.home` in your environment to reference the directory
     created above in Step #1. This depends on your operating system. For *nix,
     this might be set in a .bashrc or other startup script.
  4. Create `contact-info.properties` in the above directory. This can contain
     values that only pertain to this portlet project. Other portlets will
     ignore this file.

### Build with Database Driver

This project leverages Maven Profiles to add database drivers to the project.
Currenly only a few are included. If you need a driver that is not listed,
please open a pull request on GitHub with the additional profile you need 
(along with update to this document.)

  - `postgres` - PostgreSQL JDBC Driver JDBC 4.2 » 42.2.2
  - `mssql` - Microsoft JDBC Driver For SQL Server » 6.1.0.jre8
  - `mysql` - MySQL Connector/J » 5.1.6

To add a driver to a build, add ` -P <db-profile-id>` to the `mvn` command.
For example `mvn -P mssql clean install`.

### Build/Deploy with Maven

This project uses Maven. The basic build command is `mvn clean package`
where `clean` deletes build artifacts and `package` compiles and creates
the war file for ths project. To move the generated binary to the local
Maven cache, run `mvn clean install` instead. It will perform `package`.

One of the build steps is to check if the code meets the Google style guide.
As such, the build will fail. The style module also has a format feature,
so it is a decent idea to always call this when building. The format command
is `fmt:format`, so a build might be called as `mvn fmt:format clean install`.

As mentioned in the previous section, different drivers can be added to the
project War file. Add the profile flag as detailed above.

Deployments to Tomcat can be done with Maven. This command can also handle the
the tweak to the War file needed for Pluto, the Portlet engine in Tomcat.
The Maven target is `org.jasig.portal:maven-uportal-plugin:deploy-war` and it
takes a parameter, `-Dmaven.tomcat.home=<dir>` to know where to copy the final
War file. Adding the `pluto` profile will set up the War with the Pluto changes.

So an example build command for a deployment that uses MS SQL Server with a
Tomcat at `/home/bjagg/work/tomcat` would like like:
`mvn -P 'pluto,mssql' fmt:format clean install org.jasig.portal:maven-uportal-plugin:deploy-war -Dmaven.tomcat.home=/home/bjagg/work/tomcat`

Consider adding `maven.tomcat.home` to your system environment to drop that last
part of the command.

## <a name="config"></a>Configuration

Portlets are configured with portlet preferences. The contact info portlets come
with some preferences set to sample and testing values. These should be changed
for your environment and institution.

### Student Contact Info Preferences

  - `override.check.window` - For testing outside of the time periods defined.
    Should be set to `false` in production.
  - `terms.url` - JSON feed that defines the semester start dates. See sample `validTerms.json`.
  - `contactInfo.url` - JSON feed for student contact data. This can take a token in the URL
    that will substituted by user data. The user attribute needs to be added to the 
    `portlet.xml` file. See sample `student.json` for format. Example is 
    `http://sis.myschool.edu/user/contact-info/{username}`
  - `contactInfo.userId` - Source of the user attribute to use in the feed. In the example above,
    this would be set to `username`.
  - `race.url` - JSON feed of races to display. See sample `race.json`.
  - `ethnicity.url` - JSON feed of ethnicity to display. See sample `ethnicity.json`

### Employee Contact Info Preferences

  - `override.check.window` - For testing outside of the time periods defined.
    Should be set to `false` in production.
  - `cycle.days` - how many days between prompting user to update information.
    Defaults to `90`.
  - `directoryInfo.url` - JSON feed for employee contact data. This can take a token in the URL
    that will substituted by user data. The user attribute needs to be added to the 
    `portlet.xml` file. See sample `employee.json` for format. Example is 
    `http://sis.myschool.edu/user/directory-info/{username}`
  - `directoryInfo.userId` - Source of the user attribute to use in the feed. In the example above,
    this would be set to `username`.

## <a name="help"></a> Where to Get Help

The <uportal-user@apereo.org> mailing list is the best place to go with
questions related to Apereo portlets and uPortal.

Issues should be reported at <https://github.com/bjagg/ContactInfoPortlet/issues>.
Check if your issue has already been reported. If so, comment that you are also
experiencing the issue and add any detail that could help resolve it. Feel free to
create an issue if it has not been reported. Creating an account is free and can be
initiated at the Login widget in the default dashboard.

## <a name="contrib"></a> Contribution Guidelines

Apereo requires contributors sign a contributor license agreement (CLA).
We realize this is a hurdle. To learn why we require CLAs, see
"Q5. Why does Apereo require Contributor License Agreements (CLAs)?"
at <https://www.apereo.org/licensing>.

The CLA form(s) can be found <https://www.apereo.org/licensing/agreements> along
with the various ways to submit the form.

Contributions will be accepted once the contributor's name appears at
<http://licensing.apereo.org/completed-clas>.

See <https://www.apereo.org/licensing> for details.
