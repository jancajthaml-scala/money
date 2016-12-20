## Money

Representation of money format with lazy initialization of underlying BigDecimal prioritizing pefrormance and resources saving.

### Assumtions at implementation

Assumtions are that are monetary operations are prioritly logged in each Envelope and or each object that holds said object, most operation done on money in transaction system or conector system are retrieval are recovery from resources layer, storage to resource layer and on-demand comparation and basic aritmetic operations. Money cannot implicitly perform algebraic operations accross currencies

### Implementation details

Money class uses lazy underlying BigDecimal initialization for comparing and algebraic operations, otherwise its less resource intensive and more performant for "recovery + log + store" cycle

### Performance notes

Money format uses BigDecimal implementation from java.math.BigDecimal instead of scala.math.BigDecimal for performance reasons (java.math is slighter faster becaouse there is one less fascade Object created in construction) and because all operators are implemented in Money class with currency check and lazy underlying initialization.

### Developer comments

In previous iterations there were several implementations made by me to implement aritmetics and comparsion from scratch, I made the decision to use BigDecimal as underlying value instead for readability and realibility purposes, for arbitrary precision aritmetics and or infinite (resources based) precision aritmetics represented by underlying buffer of int/short/char code cannot be readable and performant at the same time (comparable or exceeding current BigDecimal implementations in both Java and Scala). array copy with offset lookup, dynamic type casting, break with goto statements in named blocks and so on. If you are interested in my take on Arbitrary presision Real number please take a look at my older repository (https://github.com/jancajthaml/rabbit/tree/master/src/math). I decided to prefer readability and shortness of code over performance gain in basic algebraic operations because I found this not to be major use-case of Money class.
