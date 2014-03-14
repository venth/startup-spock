startup-spock
=============

Goals
-------------
The goal of this repo is to show:

* minimal dependency setup to run Spock tests
* differences between usage Spock convention and DSL convention
* dealing with stubbing static final methods in Spock

Description of modules
-------------
* domain - contains the classes related to a tested case
* simple - contains tests that uses only Spock and stubbing / mocking provided by Spock
* simple-dsl - contains the same tests as in simple but written in DSL