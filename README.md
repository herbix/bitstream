# Bit Stream

Bit streams in Java.

## Function

Provides four stream:

1. BigEndianBitInputStream
2. BigEndianBitOutputStream
3. LittleEndianBitInputStream
4. LittleEndianBitOutputStream

A BitInputStream reads bytes from an InputStream. Users can read bits from it.
Users can write bits to a BitInputStream. It writes them to an OutputStream.

## Usage

This is a maven project. You can build it via maven, or just open it in intellij or eclipse.

```
BigEndianBitInputStream in = new BigEndianBitInputStream(anInputStream);
int data1 = in.read(3);     // reads 3 bits
int data2 = in.read(5);     // reads 5 bits
int data3 = in.read(32);    // reads 32 bits
in.close();                 // anInputStream will also be closed


BigEndianBitOutputStream out = new BigEndianBitOutputStream(anOutputStream);
out.write(0x78563412, 32);  // writes 32 bits
out.write(2, 3);            // writes 3 bits
out.write(3, 5);            // writes 5 bits
out.close();                // anOutputStream will also be closed
```

## Notes

Use of these codes is subject to [License](LICENSE) terms.

Feel free to create issues, make pull requests, or contact me [herbix@163.com](mailto:herbix@163.com).
