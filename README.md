## Money

Representation of money format with infinite (resources based) precission

### Assumtions at implementation

Assumtions are that are monetary operations are prioritly logger in state of storage, recovery and calculations, most operation done on money in transaction system or conector system are retrieval, addition and subtraction.

### Implementation details

Money object format uses companion Real class that hase value as string underlying representation of monetary value, all calculations are done over this value (String -> Array[Char]), aritmetics are done by trivia algebra no exponent, no mantisa, no arbitrary precision aritmetics, raw unrounded value with "infinite precision" (based on resources space) with controlled rounding (e.g. for financial or asset based money value ... 2 decimal places).
Only thing that are remembered are position of decimal place and signum.

Algebraic axioms are met when ommiting deep equality of trailing and leading zeroes (e.g. 00001 = 1 = 1.00000)

### Typeless aritmetics achieved with hybridized language

To achieve compatitive/faster performance to available Monetary types (BigInt, BigDecimal) calculations are done on Char interpreted as Int stored as Char shifted as Bits, for this very reason calculations were implemented in less type strict Java with static methods returning Set[Object] interpreted in Real class as Boolean, Array[Char], String...

### Performance notes (INFO SOME CLAIMS TO BE IMPLEMENTED)

Money object with its companion Real class have 2-3x faster performance in peaks, infinitelly more perfomance in resource storage and recovery (because no serialization is being done implicitly), in calculation "1.0" + "0.2" = "0.3" only result is serialized. Performance tests are given for usage scenario descripted above, to achieve serializable format of BigDecimal one needs to call `scala.math.BigDecimal("1").underlying.stripTrailingZeros().toPlainString()` whereas same result should be returned with `Real("1").toString()` for these scenarios BigDecimal costs 4.12ms for 1000 iterations.
