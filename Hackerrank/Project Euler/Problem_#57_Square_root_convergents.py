n = int(input())

answer = []
a = 3
b = 2

for i in range(1, n+1):
	if(len(str(a)) > len(str(b))):
		answer.append(i)
	c = 2*b + a
	b = a + b
	a = c
	

for i in answer:
	print(i)