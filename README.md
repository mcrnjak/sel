# SEL
SEL (Simple Expression Language) is an expression language for Java.

* [Usage](#usage)
* [Data Types](#data-types)
* [Operators](#operators)
* [Functions](#functions)
* [Object Identifiers](#object-identifiers)
* [Expression Context](#expression-context)
* [Methods and Properties](#methods-and-properties)

## Usage
```java
String input = "this.greeting + ' from ' + class('mc.sel.util.StringUtils').join(' ', this.words)";

// tokenize input
Tokenizer tokenizer = new RegexTokenizer();
List<Token> tokens = tokenizer.tokenize(input);

// parse tokens
Parser parser = new DefaultParser();
ParseTreeNode parseTree = parser.parse(tokens);

// context object
Map<String, Object> obj = new HashMap<>();
obj.put("greeting", "Hello");
obj.put("words", Arrays.asList("Simple", "Expression", "Language"));
ContextObject ctxObject = new MapContextObject(obj);

// expression context
Context ctx = new DefaultContextImpl(ctxObject);

// interpret
ParseTreeVisitor<Object> interpreter = new ParseTreeInterpreter(ctx);
System.out.println(interpreter.visit(parseTree)); // outputs "Hello from Simple Expression Language"
```

## Data Types
SEL supports the following data types:
* __Integer__ e.g. `5`
* __Double__ e.g. `3.0`
* __Boolean__ `true`, `false`
* __Null__ `null`
* __String__ sequence of characters inside of single quotes, e.g. `'This is a string'`. Single quotes can be escaped 
with `\` character, e.g. `'Single \' inside'`.

## Operators
The following operators are built into the language.

### Mathematical Operators
* `+` Add numbers.
* `-` Subtract numbers.
* `*` Multiply numbers.
* `/` Divide numbers.

### String Operators
* `+` Concatenate strings.

### Boolean Operators
* `and`, `&&` Boolean _and_. If left operand evaluates to `false` right operand is not evaluated.
* `or`, `||` Boolean _or_. If left operand evaluates to `true` right operand is not evaluated.

### Unary Operators
* `-` Minus (e.g. `-5`).
* `!` Not (negates a boolean operand, e.g. `!false`).

### Equality Operators
* `eq`, `==` Equal.
* `ne`, `!=` Not equal.

### Comparison Operators
Comparison operators work with numbers, strings and dates.

* `gt`, `>` Greater than.
* `gte`, `>=` Greater than or equal to.
* `lt`, `<` Less than.
* `lte`, `<=` Less than or equal to.

### Index Operator
* `[]` Provides access to list or array elements by the specified index. Index expression must 
evaluate to a number.

```
list(1,2,3)[2]
this.keywords[1+3]
``` 

### Assign Operator
* `=` Provides ability to assign a value to an object identifier property. Left hand side expression must
be an object identifier property expression whereas right hand side can be any expression type except assignment expression.

```
this.objectName = this.objectName + ' ' + date().time
this.priorities[0] = 5
```
<br />

Operators which are not built into the language can be implemented as functions. For example, there is no modulus `%` operator but 
it is easy to implement a function `mod(a, b)`.

## Functions
SEL provides just a few built-in functions which serve more as an example of how functions can be created, but it is 
possible to implement and register new functions.

* __date__ Creates a `java.util.Date` instance. If called without arguments as `date()` returns the current time. It can 
also be used to construct a specific date `date('24.08.2017 10:30', 'dd.MM.yyyy hh:mm')`.
* __list__ Creates a list of elements (arguments). E.g. `list()` returns an empty list, whereas `list(1, "two", 3)` 
returns a list with 3 elemens.
* __class__ Creates a `java.lang.Class` object. This function is useful for invoking static methods. 
E.g. `class('java.lang.Math').pow(2.0, 3.0)`.

SEL functions implement `mc.sel.function.Function` interface. 
```java
public interface Function {
    Object execute(Context context, List<Object> args);
}
```

Custom functions need to be registered in the global functions registry.

```java
FunctionsRegistry.registerFunction("class", new ClassFunction());
```

## Object Identifiers
Object identifiers provide direct access to the context object. The only OOTB available identifier is `this` which 
returns the context object. Other identifiers can be created by implementing a `mc.sel.identifier.ObjectIdentifier` 
interface. 
```java
public interface ObjectIdentifier {
    ContextObject execute(Context ctx);
}
```

Custom identifiers need to be registered in the global identifiers registry.

```java
ObjectIdentifiersRegistry.registerObjectIdentifier("this", new ThisIdentifier());
```

Object identifiers can be chained. For example, in a tree like object structure `parent` identifier can return a parent 
of the context object, whereas `parent.parent` can return a parent of a parent.

It is possible to invoke methods and properties on identifiers.

```
this.objectName
this.get('creationDate')
```

## Expression Context
Expression context is represented by the `mc.sel.identifier.context.Context` interface. It holds a reference to a context
object and various context properties. Functions and identifiers can access these properties since they receive the
context as a parameter.

```java
public interface Context {
    public ContextObject getContextObject();
    public void setContextObject(ContextObject contextObject);
    Object getProperty(String name);
    void setProperty(String name, Object property);
}
```

### Context Object
Context object is represented by the `mc.sel.identifier.context.ContextObject` interface.

```java
public interface ContextObject {
    Object getProperty(String name);
    Object getPropertyAtIndex(String name, int index);
    void setProperty(String name, Object val);
    void setPropertyAtIndex(String name, Object val, int index);
    Object getObject();
}
``` 

SEL comes with only one implementation of this interface which is `mc.sel.identifier.context.MapContextObject`. 
This implementation wraps a `java.util.Map` object. It is possible to create other implementations, e.g.
_JavaBeanContextObject_. 

Note: when a method is invoked on an object identifier, that method is invoked via reflection on the object which is 
wrapped by the context object. On the other hand, when a property is invoked on an identifier, that call is delegated
to the `getProperty` or `getPropertyAtIndex` method on the `ContextObject`.

## Methods and Properties
It is possible to invoke a method or property on any expression. 

For example:
```
5.0.intValue()
'string'.length()
(5 * 3).intValue()
date().getTime()
date().time
this.keywords[0].toString()
```
