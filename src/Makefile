JAVAC = javac
JAVA = java
JFLAGS = --release 11 -d bin
SOURCES = TerrainType.java FireMapParallel.java FirelineParallel.java FireTask.java
ARGS ?= 500 500 17 wildfire output/run17 50000 0.05 mixed

.PHONY: all run clean

all:
	mkdir -p bin
	$(JAVAC) $(JFLAGS) $(SOURCES)

run: all
	$(JAVA) -cp bin FirelineParallel $(ARGS)

clean:
	rm -rf bin output
