n = int(input())

def dig_sum(x):
	x = str(x)
	ans = 0
	for d in x:
		ans += ord(d)-ord('0')
	return ans

answer = max([ dig_sum(i**j) for i in range(n) for j in range(n) ])

print(answer)