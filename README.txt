CompilerConstruction

🚀 README: Asquared Programming Language
Version: 1.0
Made by: Abdur Raheem (22i-0777) and Afrah Syed (22i-1008)
📌 Introduction
Asquared is a simple, structured, and easy-to-learn programming language designed for basic computations, input/output operations, and variable management.

This guide will walk you through:
✔ Keywords & their meanings
✔ Syntax rules
✔ How to write and execute a Asquared program

🔹 1. Reserved Keywords
Asquared includes the following reserved keywords that must not be used as identifiers:

Keyword	Purpose
grab	Takes user input
shout	Displays output
numba	Defines an integer variable
floatie	Defines a decimal variable
chary	Defines a character variable
nocap	Defines a true boolean variable
cap	Represents false in boolean values
constg	Defines a global constant
constl	Defines a local constant
🔹 2. Data Types
Asquared supports four primary data types:

Type	Keyword	Example
Integer	numba	numba age = 25;
Float	floatie	floatie pi = 3.14;
Character	chary	chary letter = 'A';
Boolean	bool	bool isRaining = cap;
✔ Booleans use cap (false) and nocap (true).

🔹 3. Constants
Constants are immutable variables and must be initialized at the time of declaration.

constg numba maxusers = 100;
constl floatie taxrate = 0.05;
✔ constg → Global constant
✔ constl → Local constant

🔹 4. Input & Output
✅ Taking input using grab
numba age;
grab (age);

✅ Printing output using shout
shout ("Your age is: ", age);
✔ The shout statement must be followed by parentheses ().

🔹 5. Operators
Supports arithmetic, assignment, and comparison operators.

🔹 Arithmetic Operators
Operator	Meaning	Example
+	Addition	numba sum = a + b;
-	Subtraction	numba diff = a - b;
*	Multiplication	numba product = a * b;
/	Division	floatie result = a / b;
%	Modulus	numba remainder = a % b;
^       Power   numba pow = a^b;

🔹 Assignment Operators
Operator	Meaning	Example
=	Assign value	numba x = 10;

🔹 7. Comments
✔ Single-line comments → Start with ~
✔ Multi-line comments → Enclosed between #** ... **#
~ This is a single-line comment
#**
This is a 
multi-line comment
**#

🔹 8. String Handling
✔ Strings are enclosed in double quotes " ".
✔ Example:
shout ("Hello, World!");

🔹 9. Scope & Code Structure
✔ The entire program must be enclosed within {}.
✔ Example:
{
    numba a = 10;
    numba b = 20;
    numba sum = a + b;
    
}

🚀 How to Use: 
✅ Writing a Program
Create a file with .aa extension (e.g., program.aa).
Write your code inside it.

📌 Summary
✔ YPL supports: Variables, Constants, Input/Output, Arithmetic Operations.
✔ Programs must be enclosed in {}.
✔ Comments: ~ for single-line, #** **# for multi-line.
✔ Errors must be fixed for successful execution.

From square one to squared up—happy coding with A²! 🔥🚀
