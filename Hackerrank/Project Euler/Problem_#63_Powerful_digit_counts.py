import math 

n = int(input())

a = 10 ** (n-1)
b = 10 ** (n)

k = int(math.ceil(pow(a, 1/n)))
ans = 0
while(k ** n < b):
	ans += 1
	print(k ** n)
	k += 1


