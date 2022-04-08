t = int(input())
fibs = {}
a = 1;
b = 1;
mx = 2
M = 5002
i = 2
while(len(str(b)) < M):
    c = a+b;
    a = b;
    b = c;
    i += 1
    while(len(str(b)) >= mx):
        fibs[mx] = i
        mx += 1
    
    
for T in range(t):
    n = int(input())
    
    print(fibs[n])