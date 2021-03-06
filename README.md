hamcrest-text-patterns
======================
[![Build Status](https://api.travis-ci.org/unix-junkie/hamcrest-text-patterns.png?branch=master)](https://travis-ci.org/unix-junkie/hamcrest-text-patterns)
[![codecov.io](http://codecov.io/github/unix-junkie/hamcrest-text-patterns/coverage.svg?branch=master)](http://codecov.io/github/unix-junkie/hamcrest-text-patterns?branch=master)

A library for writing readable, composable regular expression matchers that integrates cleanly with [Hamcrest](http://code.google.com/p/hamcrest/).

Features
--------
* Easier to read (although more long-winded) than regular expressions.
* Named capture groups: captured text is identified by name, not by the index of the group.
* Composable: patterns can be easily combined into more complex patterns without worrying about breaking regex syntax or changing group identifiers.
* Refactoring friendly: patterns can be refactored with your favourite IDE.

Parsing
-------
Here's a simple example of using Hamcrest Text Patterns to parse date ranges, such as "12 Apr 1994 - 16 Mar 2005".

We can define components of the final text pattern using the factory methods defined in the Patterns class:

```java
PatternComponent month = either(
  "jan", "feb", "mar", "apr", "may", "jun", 
  "jul", "aug", "sep", "oct", "nov", "dec");

PatternComponent digit = anyCharacterInCategory("Digit");

PatternComponent date = separatedBy(whitespace(),
  capture("day", exactly(2, digit)), 
  capture("month", caseInsensitive(month)), 
  capture("year", exactly(4, digit)));
```

We can combine these into a PatternMatcher for matching date ranges:

```java
PatternMatcher dateRange = new PatternMatcher(separatedBy(whitespace(),
  capture("from", date), "-", capture("to", date)));
```

We can now parse date ranges from input text and extract the dates using the names of the capture groups we defined in the dateRange pattern: "from" and "to".

```java
String input = "31 Dec 2003 - 16 aug 2008";

Parse parse = dateRange.parse(input);

assertThat(parse.get("from"), equalTo("31 Dec 2003"));
assertThat(parse.get("to"), equalTo("16 aug 2008"));
```

And, because we defined capture groups in our date pattern, we can extract the parts of the "from" and "to" dates:

```java
assertThat(parse.get("from.day"), equalTo("31"));
assertThat(parse.get("from.month"), equalTo("Dec"));
assertThat(parse.get("from.year"), equalTo("2003"));

assertThat(parse.get("to.day"), equalTo("16"));
assertThat(parse.get("to.month"), equalTo("aug"));
assertThat(parse.get("to.year"), equalTo("2008"));
```

Matching
--------
A PatternMatcher is a Hamcrest matcher, and so text patterns can be used with libraries that build upon Hamcrest, such as [JUnit](http://www.junit.org/) or [jMock](http://www.jmock.org/). For example:

```java
String input = "31 Dec 2003 - 16 aug 2008";
assertThat(input, dateRange);

String badInput = "31 12 2003 - 16 08 2008";
assertThat(badInput, not(dateRange));
```

Writing New Pattern Components
------------------------------
The library does not cover all the Java regex functionality yet, so you may need to extend the library to add a feature you need.

For example, lets write a pattern component that matches ASCII control characters.

We must define a class that implements the PatternComponent interface.

```java
public class AsciiControlCharacter implements PatternComponent {
  ...
}
```

Then, we must implement the `buildRegex` method to append the regex syntax for the pattern component to the StringBuilder.

```java
public class AsciiControlCharacter implements PatternComponent {
  public void buildRegex(StringBuilder builder, GroupNamespace groups) {
    builder.append("\\p{Cntrl}");
  }
}
```

(We can ignore the groups parameter. That's used by the capture(...) and valueOf(...) pattern components to identify capture groups by name.)

Finally, we should write a static function to make patterns that use this component easy to read. For example, we could put factory functions for all the pattern components we need for our project in a single class and statically import them into the classes that use them.

```java
public class ProjectSpecificPatterns {
  public static PatternComponent asciiControlCharacter() {
    return new AsciiControlCharacter();
  }
}
```

Maven
-----
To use hamcrest-text-patterns with Maven 2/3, you must add the following repository to your project's `pom.xml` file:

```xml
<repository>
  <id>hamcrest-text-patterns-repo</id>
  <name>hamcrest-text-patterns repository on GitHub</name>
  <url>http://wcmatthysen.github.com/hamcrest-text-patterns/repository/</url>
</repository>
```

Then, you can add the following dependency:

```xml
<dependency>
  <groupId>org.hamcrest</groupId>
  <artifactId>hamcrest-text-patterns</artifactId>
  <version>0.0.1</version>
</dependency>
```

License
-------
BSD License

Copyright (c) 2000-2008, www.hamcrest.org
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list of
conditions and the following disclaimer. Redistributions in binary form must reproduce
the above copyright notice, this list of conditions and the following disclaimer in
the documentation and/or other materials provided with the distribution.

Neither the name of Hamcrest nor the names of its contributors may be used to endorse
or promote products derived from this software without specific prior written
permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
DAMAGE.
