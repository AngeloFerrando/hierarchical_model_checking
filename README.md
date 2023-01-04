# Hierarchical Model Checking

A Practical Approach to Verify Hierarchical Systems.

## How to install

- YASM: http://www.cs.toronto.edu/~arie/yasm/QuickStartYasm.txt

## How to run

To run YASM just type the following in the terminal

```bash
yasm -p '<CTL-property>' <C-program>
```

## How to run benchmarks

To run the extended version of YASM with the hierarchical model checking feature type the following in the terminal

```bash
python experiments.py <repetitions> <min> <max> <step>
```

Where:
- repetitions: it is the number of times the YASM model checker is called for each experiment (to improve reliability of results).
- min: minimum number of function calls.
- max: maximum number of function calls.
- step: increment step

Example:

```bash
python3 experiments.py 5 5 50 5
```

It runs the benchmarks 5 times for each verification step, considering files containing function calls from 5 to 50, with a step of 5.
