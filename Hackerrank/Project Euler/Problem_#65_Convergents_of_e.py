N = 30000 + 5
v = [ (i//3 + 1)*2 if i%3 == 1 else 1 for i in range(N)]

n = int(input())
if(n == 1):
	print(2)
	exit()
	
p = n-2;
den = v[p];
num = 1;
while(p != 0):
	p -= 1
	num += den*v[p]
	a = num
	num = den
	den = a

num += den*2;

# print("{}/{}".format(num, den))
ans = 0;
while(num != 0):
	ans += num%10;
	num = num // 10;
	
print(ans)