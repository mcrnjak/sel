# SEL
SEL stands for Simple Expression Language. It was created as an exercise of writing a parser and an interpreter for expressions. It is still a work in progress.

## Usage
```java
String input = "this.aaa + ' ' + class('mc.sel.util.StringUtils').join('|', list('1','2','3')) + ('|' + (this.ccc.get(1) - this.bbb - 1).intValue())";

// tokenize the input
Tokenizer tokenizer = new RegexTokenizer();
List<Token> tokens = tokenizer.tokenize(input);

// parse the tokens
Parser parser = new DefaultParser();
ParseTreeNode parseTree = parser.parse(tokens);

// stringify as lisp structure
ParseTreeVisitor<String> stringifier = new ParseTreeLispStringifier();
System.out.println(stringifier.visit(parseTree));

// context object
Map<String, Object> obj = new HashMap<>();
obj.put("aaa", "Hello");
obj.put("bbb", 10);
List<Integer> list = new ArrayList<>();
list.add(5);
list.add(15);
obj.put("ccc", list);

// interpret
ParseTreeVisitor<Object> interpreter = new ParseTreeInterpreter(new MapContextObject(obj));
System.out.println(interpreter.visit(parseTree)); // outputs "Hello 1|2|3|4"
```

## Data Types
SEL supports the following data types:
* __Integer__ e.g. `5`
* __Double__ e.g. `3.0`
* __Boolean__ `true`, `false`
* __Null__ `null`
* __String__ sequence of characters inside of single quotes, e.g. `'This is a string'`. Single quotes can be escaped with `\` character, e.g. `'Single \' inside'`.

## Operators
The following operators are built into the language.

### Mathematical Operators
* __+__ Adds numbers or cancatenates strings
* __-__ Subracts numbers
* __*__ Multiplies numbers
* __/__ Divides numbers

### Boolean Operators
* __and__ __&&__ boolean and. If left operand evaluates to `false` right operand is not evaluated.
* __or__ __||__ boolean or. If left operand evaluates to `true` right operand is not evaluated.

### Equality Operators
* __eq__ __==__ equal
* __ne__ __!=__ not equal

### Comparison Operators
Comparison operators work with numbers, strings and dates.

* __gt__ __>__ greater than
* __gte__ __>=__ greater or equal than
* __lt__ __<__ less than
* __lte__ __<=__ less or equal than

Operators which are not available can be implemented as functions. For example, there is no modulus `%` operator but it is easy to implement a function `mod(a, b)`.

## Functions
SEL provides just a few built-in functions which serve more as an example of how functions can be created, but it is possible to implement and register new functions.

* __date__ Creates a `java.util.Date` instance. If called without arguments as `date()` returns the current time. It can also be used to construct a specific date `date('24.08.2017 10:30', 'dd.MM.yyyy hh:mm')`.
* __list__ Creates a list of elements (arguments). E.g. `list()` returns an empty list, whereas `list(1, "two", 3)` returns a list with 3 elemens.
* __class__ Creates a `java.lang.Class` object. This function is useful for invoking static methods. E.g. `class('java.lang.Math').pow(2.0, 3.0)`.

SEL functions implement `mc.sel.function.Function` interface. Custom functions should be added to the global functions registry.

```java
FunctionsRegistry.registerFunction("class", new ClassFunction());
```

## Object Identifiers
Object identifiers provide direct access to the context object. The only OOTB available identifier is `this` which returns the context object. Other identifiers can be implemented by implementing a `mc.sel.identifier.ObjectIdentifier` interface and need to be registered as

```java
ObjectsRegistry.registerObjectIdentifier("this", new ThisIdentifier());
```

Object identifiers can be chained. For example, in a tree like object structure `parent` identifier can return a parent of the context object, whereas `parent.parent` can return a parent of a parent.

It is possible to invoke methods and properties on identifiers.

```
this.aaa
this.get('aaa')
```

## Methods and Properties
It is possible to invoke a method or property on any expression. 

For example:
```
5.0.intValue()
'string'.length()
(5 * 3).intValue()
date().getTime()
date().time
```
