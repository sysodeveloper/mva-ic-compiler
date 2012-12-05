#!/bin/bash

# Build the project
ant

cd bin
# ex1 Tests

#echo
#echo "Running tests:"
#TEST_FILES=../test/ex1/*.ic
#for file in $TEST_FILES
#do
#  echo "Testing $file:"
#  java IC/Compiler $file
#done

echo
echo "Specific tests:"
CHECK_FILES=../test/checks/*.ic
i=1
for file in $CHECK_FILES
do
  echo "Checking $file:"
  java IC/Compiler $file > "../test/checks/res$i"

  if diff "../test/checks/res$i" "../test/checks/exp$i";
  then
    echo "pass"
  else
    echo "failed"
  fi
  i=i+1
done

cd ..
