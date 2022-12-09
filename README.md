## CS 160 Honors Project
#### By Brian Waltz 

Hello! I write this both as a piece of my commenting, and as an exploratory for the project because I think that'd be friggin awesome. However, before I officially begin, I would like to note a few things. One, this was a short and sweet project, and so it lacks depth in certain aspects (sometimes you quit while ahead~). Two, I started programming Java like 3 months ago, or whenever this class started, so I'm not neccessarily up to all of the standards and expectations of Java. Three, I'm imperfect. For those reasons, expect to see bgug, poorly implemented operations/functions, or bad explanations, at some point along the line. Without further ado, allow me to start. 

### Using The Project

This project was made to enable the quick design of CPU simulators. Specifically, it's supposed to handle the register operations, because that's a simple and quick way to make the whole process easier. To demonstrate how that works, we will implement a simple CPU. This will be a higher level utilization, and so reference the "Making The Project" section below if needed. 

First, we need to decide on how we want the machine code of our CPU to be structured. I personally opted for a command-type/command-size prefaced style, both because of simplicity, and because this is a simulator not an emulator. 

A command-type prefaced style means that the CPU will read one byte to decide the *mode* it uses to evaluate the next byte. Ie, we could have a 4 byte, 2 byte, and register mode, and the CPU could decide how to pass parameters based on that. 

Beyond that, I think having individual Op-codes (byte values) for individual functions is better than a convuluted series of state bytes prefacing an instruction. Because we already have one preface byte, we can re-use instruction values if we so choose, for different sections. 

Let's get onto using the actual CPU registers. This CPU simulation library was designed to be used via Java reflection, and thus, expects the user to define methods, and fields corresponding to what those methods access. 

We will implement a CPU with one mode, an int mode, that reads bytes, and moves ints into our registers. The general program could be represented by the following psuedocode:

```Java
while bytes remain:
    read a byte from instructions:
        Jump to the location corresponding to the value
            value is equal to (int_value):
                Access target class
                Retrieve method specified by next byte
                read 4 bytes // int
                pass bytes to method, execute method
            ... (other values)
```

#### A simple CPU

let's make a simple CPU with 4 32 bit registers, and no memory. First, we need to create the registers. 

```Java
public class CPU{
    public CPU_R_32 ESI = new CPU_R_32();
	public CPU_R_32 EAX = new CPU_R_32();
	public CPU_R_32 EDX = new CPU_R_32();
	public CPU_R_32 EBX = new CPU_R_32();
    // The constructors for these construct the 
    //      neccessary members below themselves. 
}
```

We make these public so they're easier to access via Reflection. 

Beyond that, we need to define a few instructions. Let's start with a move instruction, in the 4 byte size range. 

```Java
public class CPU {

	// Create registers with default empty constructor (it will create sub-objects from there.
	public CPU_R_32 ESI = new CPU_R_32();
	public CPU_R_32 EAX = new CPU_R_32();
	public CPU_R_32 EDX = new CPU_R_32();
	public CPU_R_32 EBX = new CPU_R_32();
	
	// Define methods under class, referencing this class' data. Allows reflection API to grab them. 
	public void movEAX(Integer data) {
		this.EAX.setValue(data.intValue());
	}
	public void movEDX(Integer data) {
		this.EAX.setValue(data.intValue());
	}
	public void movEBX(Integer data) {
		this.EAX.setValue(data.intValue());
	}
	public void movESI(Integer data) {
		this.EAX.setValue(data.intValue());
	}	
	
}

```

This has now created a CPU capable of moving 4 bytes into its registers. This CPU is a class, and we will create an instance of it whenever we need actually use it. 

Now beyond that, we need a way to evaluate those instructions. First, we must choose the mode. This example will only have one mode, int parameter mode. We will assign that the value `0xc1`. 

Once we have the mode, we want to find the function we plan to execute. 

Let's focus on that next. For this, the CPU simulation project provides the `InstructionSet` class. Let's bind our instructions to some byte values. 

```Java
//setup instruction set
InstructionSet int_mov_regs = new InstructionSet(
                            (byte)0xb5, "movEAX",
                            (byte)0xa5, "movEDX",
                            (byte)0xd5, "movEBX"
                        );
```

This binds the String "movEAX" to `0xb5`, "movEDX to `0xa5`, "movEBX" to `0xd5`. It does not bind "movESI". At present, these are simply Strings. 

We also need a way to store all of our instructions. For that, we will use a simple ByteBuffer. This could be initialized in any number of ways - from a file, from a predefined array, or dynamically - but we will simply define it statically. 

```Java 
byte[] instructions = new byte[] {
	(byte)0xc1, (byte) 0xb5, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x16,
    /*    MODE          INS         BYTE1        BYTE2        BYTE3        BYTE4*/
	(byte)0xc1, (byte) 0xb5, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x16
};

ByteBuffer memory = ByteBuffer.wrap(instructions);
```

Now that we have a CPU class, an instruction set, a condition to run different instruction sets, and a list of instructions, we need a way to actually run the methods. 

For that, we will use Reflection. Reflection refers to code modifying itself or its runtime. Java provides methods for reflection, at the cost of some speed (which is why we use hashtables and switches instead of ifs in this project). 

For the reflection class, we need to access an instance of the class we want to view or modify, get the method by its signature (referenced in Making The Project), and invoke it on an actual instance of the class or object it should affect. This is why we need a class for our CPU.

Doing that looks like this

```Java
Class<Target_Class> c = Target_Class.class;
// get a Class of type Target_Class from Target_Class
Class<?>[] TargetArgType = new Class[] {ParameterClass.class};
// creates a list of generic class type objects using the input classes
//    If we wanted to generalize this, and thus the search, we could get all
//    classes of a name, extract their paramaters, see which have parameters
//    compaitble with whatever we seek, and execute all or some of those. 
String command = "main"; 
Method ins_com = c.getMethod(command, TargetArgType);
// Searches class Target_Class (c) for the command by its name and paramaters

ins_com.invoke(ClassInstance, TargetArgType.cast(InputValue));
// invoke method Target_Class.command(TargetArgType.cast(InputValue)) on ClassInstance. 
// TargetArgType is an array of types, to it needs to be used to cast the params, 
//    and it needs to be enumerated if the entire array is to be matched
```


Putting all of this together looks something like this:
```Java
import IDK.CPUSimulator.*; // import CPUSim project, f not in project
import IDK.CPU.*; // import CPU, if not in project

public static void main(String [] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException  {
		
    CPU ourCPU = new CPU(); // instance of prior CPU class

    InstructionSet mov_regs = new InstructionSet(
                                    (byte)0xb5, "movEAX",
                                    (byte)0xa5, "movEDX",
                                    (byte)0xd5, "movEBX"
                                );

    byte[] instructions = new byte[] {
        (byte)0xc1, (byte) 0xb5, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x16, 
        (byte)0xc1, (byte) 0xb5, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x16
            };

    ByteBuffer memory = ByteBuffer.wrap(instructions);

    Class<CPU> c = CPU.class;

    while (memory.hasRemaining()) {
        byte curr_ins = memory.get();
        switch(curr_ins) {
            case (byte) 0xc1:
                Class<?>[] argType = new Class[] {Integer.class};
                byte instruction = memory.get();
                // moving 4 bytes into register
                int value = memory.getInt();
                String command = mov_regs.searchINS((Byte) instruction);
                Method ins_com = c.getMethod(command, argType);
                // we don't handle possibility it might error out, CPU would be reading invalid bytecode then
                ins_com.invoke(ourCPU, argType[0].cast(value));

                // These call on other methods from CPUSimulator.CPU_R
                // They just print out the values to prove this works. 
                System.out.println(ourCPU.EAX.getValue());
                System.out.println(": " + CPU_R.registerToBinString( (int) ourCPU.EAX.getValue()) );
        }
        

    }
}
```

This is the example given in the main executable of the project, and is an extremely simple instance. It doesn't apply and AND, OR, XOR, NOT, Addition, Multiplication, or even Subtraction to anything. However, it is a demonstration of the intended use of the project. 

Beyond this, I would suggest uses of it use implementations of AND, OR, etc; From the class. This is best supported for 32-bit (because that's what was intended) but it applies elsewhere, and is described below. 

Additionally, while the above method already conforms an instruction set fitting a parameter requirement to a state of the CPU, the state switch itself, alongside the method/function calling/passing logic, could easily be moved into `CPU_R` as methods - similar to how `CPU_R_32`'s register by name functions work - to make it such that all a CPU-Simulation designer needs to do is create and specify an instruction set, and specify a few bytes for the different modes. 

I avoided implementing such mainly because, I'm lazy. Hopefully however, I explained well enough, and if not, maybe give the section below a shot? 

### Making The Project
For my honors project, I was tasked with a CS project of my choice in nature and area, in Java. The requirements for me were as follows:
- The project must include and apply computer science concepts from at least 3 chapters of material covered in the course. 
- In general, the project should be around 1.5 times the size of the other course projects (and can be more).
- A project proposal detailing the project, its purpose, and what it will do. 
- The proposal should include a general plan of implementation to apply the course content

With this in mind, I eventually decided to do a CPU simulator. Working on the simulator, I decided to create a more generalized set of classes & mindset for creating a CPU simulation, alongside a small and simple one using that. 

First is the project breakdown. I will focus on the goals, and we will work up from the goals into the most basic implementations, and scale upwards. 

#### A CPU

A CPU is the processing center of a computer, and in a generalized sense, is a hardware implemented state machine. More specifically, a deterministic finite-state machine. 
- Deterministic → Its future operations can be decided by seeing its instructions, a.k.a it has solid rules. 
- Finite → It has a limit to the amount of instructions, and will stop when that limit is reached

- State-machine → it keeps track of a state (often a flag variable) or mode that it uses to decide what it does (think video game ai, aware state, guard state, pursuit state, etc;)
To achieve these goals, CPUs use hardware registers, and operations on those registers. Those operations rely on moving values around and between registers and memory, but for now, we will leave that be. 

The CPU we (or I) aim to implement is 32-bit. This means each register created has 32 binary switched allocated to it. This has meanings itself, but those can be ignored for now. Just know that the value of a 32 bit register is in this format:

>00000000000000000000000000000000

Where any of those 0s could be a 1 to represent a value.

Beyond that though, for convenience, 32-bit registers can be accessed in multiple modes, and used as if smaller than they are. This references older archtitetures of computers in particular, but for us, imagine it being a way to be more specific. 

I'm specifically going to be modeling the way that Intel and other modern CPUs do registers, but feel free to ignore this. 

Modern 32-bit CPUs (and by modern I mean made decades ago) allow you to access the full register, the lower half of the register, or either half of the lower half of the register. Thus, to visualize this, let's cut it up. 

Let's take a 32-bit register, we'll say PIM, and visualize its bits. 
>00000000000000000000000000000000

Splitting PIM in half would result in two halves:

>0000000000000000    0000000000000000

Of which we can access only the lower, which we'll call PM. 

>PM  : 0000000000000000

However, we can access both halves of PM. Splitting it in half results in two 8-bit length values. Since there will be a high half (only used for high values), and a low half (only used for low values), we'll call them high and low. So, let's name the registers MH, and ML, for M high, and M low respectively. 

>MH: 00000000

>ML: 00000000

Let's visualize our breakdowns a bit. 

```python
                PIM
===================================
       N/A       |       PH      
=================|=================
  N/A   |  N/A   |   MH   |   ML
========|========|========|========
00000000|00000000|00000000|00000000
```

That hopefully makes it more clear than the explanation alone. 

However, back to the point. We know what we need to make, and what it needs to do. We need to make a deterministic finite-state machine, using these registers and the values in them. So, to begin making any progress, let's make the registers. 


#### 8-bit Register

As shown above, the CPUs registers can be broken into 8-bit blocks. Now, when attempting to represent or create something, it's often useful to think of things similar to what we wish to represent. Luckily for us, 8-bit registers are rather related to bytes, and thus, a byte is an awesome way to represent them, because a btyt has 8-bits. 

In Java specifically, bytes (not Bytes, notice capitalization) are primitives. They don't really have a low of methods, and we especially can't easily use them as registers on their own. Because of this, we need to wrap this primitive in something of our own. 

To wrap up some data, we can use a class. A class is a way of organizing and pairing up different data and methods/functions. 

```Java
public class CPU_R_8 {
    byte value; // The value we're wrapping 
}
```

This class we've just created, is simply an object that bundles the data we store in it. Instead of directly accessing value, we access an instance of CPU_R_8. So for instance, we could do something like this:

```Java
CPU_R_8 our_8bit_register = new CPU_R_8();
our_8bit_register.value = (byte) 255; // the maximum value of a byte (2^8-1, because 0 is counted)
```

This code example creates an instance of our class, using what's called an implicit constructor. Implicit just means it's done without us doing anything, because Java requires us to have a constructor. 

This class instance however, has a field called value. This field is just a byte, and we can otherwise treat it as one. 

However, we don't want to provide the byte. If we let people access the byte alone, they might screw stuff up (not everyone can be as brilliant as us in CS-160) so we need to prevent that. 

```Java
public class CPU_R_8 {
    private byte value; // The value we're wrapping 
    // ^ access modifier
}
```

What we've done up above is add an access modifier, private. All we need to know about this is that is makes Java hide the field (our byte named value) from other classes and functions. Hurray! Now they can't screw it up. 

But. They also can't use it. 

Uh. Let's fix that too. To do that, we need to provide the neccessary methods for them to do anything that they might want to do to this register. Let's divide these into two bits:
- Fundamental methods - these are the bare-bones methods needed to use it. Particularly the ability to set and read the value of the variable. From there, they can write their own methods to do wack stuff. 
- Luxury methods - stuff we implement so that they don't have to modify our fundamentals into oblivion. 

Let's write our fundamentals. 

The most basic functions we need, in this case at least, are functions to get and set our byte. Consider the role. We are writing something for reading and writing a byte meant to be used to store numeric data. Thus, we should return a numeric type, and probably a byte value. 

```Java
public class CPU_R_8 {
    private byte value; // The value we're wrapping 
    // ^ access modifier
    
    public byte getValue()/* returns a byte*/{
        return value;
    }
}
```

Our get value function is pretty dang simple. Our field `value` is already a byte, so returning it is as simple as that. We need no inputs, and we make the function public because we're fine with developers putting their grubby hands on it. 

To set our byte, we can do similar. 

```Java
public class CPU_R_8 {
    private byte value; // The value we're wrapping 
    // ^ access modifier
    
    public byte getValue(){
        return value;
    }

    public void setValue(byte value_in){
        this.value = value_in;
        // "this" refers to the object executing the code
    }
}
```

Boom. We now have a function we can call that sets and gets our value. You might think that feels a bit redundant, and while I can't *completely* argue with that, I will say that small steps like this in implementing data protection can save you down the line. For instance, consider if we wanted to be able to convert a string or int value of a byte, into a byte, and set our field `value` equal to it. 

If we allow those _**devious**_ developers to do it themselves, they might try to write something like this:

```Java
CPU_R_8 our_8bit_register = new CPU_R_8();
our_8bit_register.value = Byte.parseByte("255");
```

Which would be completely valid. However, the `Byte.parseByte()` method checks the bounds of the string before conversion. What is it didn't? What if they use an alternative, or custom method? What if they use a method that works, but they mess it up somewhere and don't notice? 

By taking the need to do this out of their hands, we prevent bugs higher up the chain. We can _know_ that we're converting the string to a value properly, if we _know_ the function we use does. And when the developer is us (me) it's a lifesaver. 

But back to the topic at hand. Computing, Registers, BYTE VALUES, all these words. We didn't say them to just make a getter and setter. What else could this class use? 

First off, if we want using it to go well, we should add a constructor or two, so that we know it's being initialized correctly. 

```Java
public class CPU_R_8 {
    private byte value; // The value we're wrapping 
    // ^ access modifier
    CPU_R_8(){
        this.value = 0;
    }
    
    CPU_R_8(byte byte_in){
        this.value = byte_in;
    }
    
    ...
}
```

I cut the other functions we made earlier off of this snippit for brevity, so focus on the constructors. 

Constructors are methods/functions with the name of the class, that Java calls by matching their parameter signature. The parameter signature is the series and type of parameters passed to the function. For the first, the parameter signature is empty. For the second, the parameter signature is [byte]. This will be important far far later, but for now just keep a minor note. 

We create two, a simple empty constructor which calls when a user specifies none, and a constructor accepting an initial byte value. For the purpose of further following of this documentation, you need the empty constructor. The byte input constructor is extra. Following this, I will try to rely more on code-flow and in code comments to explain what exactly code is doing. When deemed neccessary, I will still explain concepts in detail. 


But *now* that's it! If we want these registers to be more easily useful, we can give operations to them. Specifically, we can do the simple bitwise operations: AND, OR, NOT, and XOR. 

The functions are bit by bit, on the nth (indexed at n) position, and work as follows:
- AND → Maintains the value if both values at the nth position are the same
- OR → Maintains a one in the nth position if either of the binary series has a one in that position
- NOT → returns a binary series with all of the bits flipped to their opposite
- XOR → Maintains a one in the nth position if and only if *one* of the binary series has it in that position. 

Examples: 
- 10001111 AND 10111000 = 10001000
- 00001100 OR  10100000 = 10101100
- NOT 10110111 = 01001000 
- 11100111 XOR 10111000 = 01011111

To see it more clearly, stack the binary series atop one another, matching their positions. I do this multiple times in the comments of the actual project. 

Luckily for us, like most languages, Java provides a series of bitwise operators (you can look it up) that we can steal. 

```Java
public class CPU_R_8 {
    private byte value; // The value we're wrapping 
    ... // constructors
    public void AND(byte byte_in){
        this.value = (this.value & byte_in) & 0xff;
        // parenthesis for precedence
    }

    public void XOR(byte byte_in){
        this.value = (this.value^byte_in) & 0xff;
    }

    public void NOT(byte byte_in){
        this.value = (~this.value) & 0xff;
    }

    public void OR(byte byte_in){
        this.value = (this.value | byte_in) & 0xff;
    }

    ... // getters & setters
}
```

I said I would stop explaining stuff as much just a bit too soon. 

You're likely wondering why I included the AND operation on all of those, and you might be wonder what `0xff` is. 

First, Java doesn't like primitives, and so it mistreats them. When we do certain operations on bytes and bits in Java, or certain operations involving bitwise operations between unequal data types, Java will mess with the values. To keep the values the same as they originally are, we perform what's known as an identity operation. 

An identity operation is an operation that returns its input as output. For instance, multiplying by one returns an identity. AND can be seen as multiplying the bits of two binary sequences. Thus, 

> 1011 AND 0001 = 0001

is the same as saying 

> 1\*0 0\*0 1\*0 1\*1 = 0001

This ties into what `0xff` is. It's hexadecimal, a base16 notation, for 16*16, which is 256, or the value 255 (because 0 takes one up). You'll notice that it's the same as 2^8-1, the max byte value. This means its binary representation is `11111111`. 

That makes an AND operation with a `0xff` on one side an identity operation for a byte-length value. 

What this does it force Java to keep everything the same. It's not neccessarily important here, but it's a good just-in-case. 

#### 16-bit register

While you're more than welcome to continue expanding and making amazing yours, I will be moving on. We need to create our next registers after all. Remember to make this in a separate file, if you intend to actually export it from the file. 

Now we need to reference what we know again. A 16-bit register is 16 bits, so, based on above, our intuition would *likely* say to make it a `short`, which has a length of 16-bits. However, we must remember - We need to access each half. 

Now, we could still make it a short, and our methods for getting the halves could just do speciea bitmagic to get the values out. However, why not re-use our code that already does that? Why not use our code that already represents an 8-bit register, to represent the 8-bit halves of a 16-bit register? 

Doing this requires us to use references. A reference, for those who've graced by their existence thus-far, is a variable that just stores another object or variable. 

You can view this, in java at least, as having a variable that is an Object, and that maintains all of the functions of that Object. Thus, if that Object is a class, it acts as that class. 

So, let's create this. 

```Java
public class CPU_R_16 {
	// DO NOT initialize these. Make sure they're null, or empty.
	private CPU_R_8 LB = null; //Low Byte
	private CPU_R_8 HB = null; // High Byte
}
```

We now have fields, private fields, for our two objects. This means two things for us. One, we already have methods for getting both the high and low byte. Two, when working with the entire 16-bit register, it's significantly more clear which byte is low or high. Additionally, we can't accidentally screw up the operation or value of the registers by accessing the `value` field directly.

One of the most important things we can add is our constructor.

```Java
public class CPU_R_16 {
	// DO NOT initialize these. Make sure they're null, or empty.
	private CPU_R_8 LB = null; //Low Byte
	private CPU_R_8 HB = null; // High Byte

    CPU_R_16(CPU_R_8 High_byte, CPU_R_8 Low_byte){
        this.HB = High_byte;
        this.LB = Low_byte; 
    }

    CPU_R_16(){
		this(new CPU_R_8(), new CPU_R_8());
		// allow empty initialization, this calls the empty constructor
        //      and passes in empty constructors!
	}
}
```

Look at that. The fruits of our labor manifesting already. Because we were so diligent as to make our `CPU_R_8` class have an empty constructor what we know is valid, we can initialize it in the initialization of our `CPU_R_16` without worry. 

Next, let's add getters. 

```Java
public class CPU_R_16 {
	// DO NOT initialize these. Make sure they're null, or empty.
	private CPU_R_8 LB = null; //Low Byte
	private CPU_R_8 HB = null; // High Byte

    ... // constructors 

    public byte getHigh(){
        return(this.HB.getValue());
        // call the getValue() method we made
    }

    public byte getLow(){
        return(this.LB.getValue());
        // call the getValue() method we made
    }

    public short getValue(){
        short high_half = ( ( (short) this.HB.getValue()) << 8 & 0xffff)
        short low_half = ((short) this.LB.getValue() & 0xFFFF )
		return (short) ((high_half | low_half) & 0xffff);
			// Shift the bits into alignment and XOR/OR them, ie:
			/*
				Cast byte to short
				0000000011001111 HB
				0000000011111100 LB
				Shift high-byte left
				1100111100000000 HB
				0000000011111100 LB
				XOR
				1100111111111100 16-bit REG
				POG!!!
			*/
	}
}
```

Yet again, the blessings of our work strike. Now that we're starting to use the class, and especially doing big bitwise operations like this, knowing that `CPU_R_8` isn't screwy is a lifesaver.

Now allow me to explain the little `getValue` chunk. 

We have two bytes, one representing our high half, and the other our low half. Now, we don't need to separate them because we're awesome and used our previous registers instead. But, that means we must combine them to return them all. 

Luckily, that's easy. All we need to do is move our high half 8 places to the left, and use either of the XOR/OR operations from above (it doesn't matter since there's no overlap). 

That's what the `((short) HB.getValue() << 8) & 0xffff` does. It converts it to a short, so it has 16 bits instead of 8, shifts it to the left, and then ANDs it with `0xffff` which represents 16 1s, or `11111111111111111` (our identity). 

The `((short) LB.getValue() & 0xFFFF )` does the same, but doesn't shift it left. Thus, when we OR them together, we get all of the 1s in one nice short (AND with identity to keep it the same). 


With that all out of the way, we can add our setters. 

```Java
public class CPU_R_16 {
	// DO NOT initialize these. Make sure they're null, or empty.
	private CPU_R_8 LB = null; //Low Byte
	private CPU_R_8 HB = null; // High Byte

    ... // constructors 

    ... // getters

    public void setHigh(byte byte_in){
        this.HB.setValue(byte_in); // byte-in
        // setter we defined
	}

	public void setLow(byte byte_in){

        this.LB.setValue(byte_in); // byte-in
        // setter we defined 
	}

    public void setValue(short _16bit){
        // because our halves our separate already, we can just set them separately
        this.HB.setValue((byte)(_16bit << 8 & 0x00FF));
        // setter we defined
        this.LB.setValue((byte)(_16bit & 0x00FF));
        // setter we defined
	}

    // If we added a setValue with two byte inputs, since we defined 
    //      a method for setting with a short, we could use the logic
    //      from our getter to create a short of the two combined bytes
    //      and make that the input to another setValue call, ie:

    /*
     * public void setValue(byte high_byte, byte low_byte){
     *     short combined_bytes = miscellaneous_bit_magic_stuff
     *     setValue(combined_bytes);
     *  }
     */

    // That would be yet again, more code re-use (absolutely based). 
}
```

This code is pretty simple and straightforward, largely thanks to our minor efforts in `CPU_R_8`. 

Beyond that, we only need to create our 32-bit register. 

#### 32-bit register 

You've seen the drill by this point. How can we accomplish what we need to whilst writing the _**least**_ code possible? 

Well, our 32-bit register needs to have a couple of things. We need to be able to get its value, the value of the lower 16 bits, and the values of the upper and lower halves of the lower 16 bits. 

Luckily for us, we already implemented this! Let's do our fields. 

```Java
public class CPU_R_32 {
    // reference to lower 16 bits, using object
    private CPU_R_16 Low_half = null; // null initialization 
    private short High_half = null // null initialization
    // our high half, we don't need to access this individually (no getters/setters)
    // however implementing such would be easy, and you could do it. 
    // I only don't because of choice. 
}
```
Next are constructors
```Java
public class CPU_R_32 {
    ... // fields

    CPU_R_32(CPU_R_16 Low_half_in){
		this.Low_half = Low_half_in; // set the low half to an object we input
	}

	CPU_R_32(){
		this(new CPU_R_16()); // input a constructed object to empty constructor

		// supporting empty here allows an entire register series to be created
        // from top down. We know calling the empty CPU_R_16 constructor
        // will call the empty CPU_R_8 constructors
        // by making our empty, we save much work for ourselves. 
	}
}
```

Then we add all of our getters. 

```Java
public class CPU_R_32 {
    
    ... //fields 
    ... //constructors 

    public short getLowHalf(){
        // lower 16 bits
        return this.Low_half.getValue();
        // CPU_R_16 object, so it has getValue()!
        // re-use our method!!
    }
    public byte getLowHalfLow() {
        return this.Low_half.getLow(); 
        // even more re-use!
    }
    public byte getLowHalfHigh() {
        return this.Low_half.getHigh();
        // even more re-use
    }
}
```

and our setters

```Java
public class CPU_R_32 {
    
    ... //fields
    ... //constructors 
    ... // getters

    public void setValue(int value){
            this.High_half = (short) ((int) value>>16 & 0x0000FFFF);
            // remember that our high half is actually a short, so we want 
            // the bits of our int (32-bits) to be on the side that 
            // gets kept when the higher bits are cut off. 
            this.LH.setValue((short) ((int) value & 0x0000FFFF));
            // our setValue method!
        }

        public void setLowHalf(short Lh){
                // calls the setValue operation of the lower half
                this.Low_half.setValue(Lh);
        }

        // we could use CPU_R_16 setLow and setHigh to provide that 
        //      functionality through CPU_R_32. 

        // if we defined a CPU_R_16 setValue() with byte paramaters
        //    then we could do something like this as well

        /*
        public void setLowHalf(byte Lh_HB, byte Lh_LB){
            this.Low_half.setValue(Lh_HB, Lh_LB);
        }
        */
}
```
