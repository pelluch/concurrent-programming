#!/usr/bin/python

import subprocess
import sys

f1 = open('output', 'r')
f2 = open('tree', 'a')
f2.write(sys.argv[1] + '\n')

lines = f1.readlines()
lines = [line.rstrip().replace('|', '*').replace('-', '|').replace('*','-') for line in lines]
cols = max([len(line) for line in lines])

print('Columns: ' + str(cols))
rows = len(lines)
print('Rows to process: ' + str(rows))
#for line in lines:
#	print(len(line.rstrip()))

for i in range(cols):
	new_line = []
	#print('Processing col ' + str(i) + '/' + str(cols))
	for line in lines:
		if i < len(line):
			new_line.append(line[i])
		else:
			new_line.append(' ')

	new_line = ''.join(new_line)
	new_line = new_line[::-1]
	f2.write(new_line + "\n")
	f2.flush()
	#print(new_line)

f1.close()
f2.write('===================================================' + '\n')
f2.close()

#subprocess.call(['subl3', 'tree'])
