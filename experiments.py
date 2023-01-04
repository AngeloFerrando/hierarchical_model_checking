import sys
import datetime
import os


def main(args):
    repetitions = int(args[1])
    min = int(args[2])
    max = int(args[3])
    step = int(args[4])
    # kind = int(args[5])

    with open('./res.csv', 'w') as f:
        f.write('')
    for i in range(min, max, step):
        elapsed_time = [0, 0, 0]
        for kind in range(0, 3):
            for k in range(0, repetitions):
                code = '''int main()
{
    int y;
    y = 10;
    y = y-1;
    '''
                for j in range(0, i):
                    if kind == 0:
                        code = code + '\tf' + str(j) + '(10);\n'
                    elif kind == 1:
                        code = code + '\tf0(10);\n'
                    else :
                        code = code + '\tf0(10);\n'
                        break
                code = code + '''if (y>5)
    {
        ERROR:
    }
    END:
 }
 '''
                for j in range(0, i):
                    if kind == 0:
                        code = code + 'int f' + str(j) + '''(int n){
    int x=0;
	if(x < n){
		x++;
	}
	return 0;
}
'''
                    elif kind == 1:
                        code = code + '''int f0(int n){
   int x=0;
   if(x < n){
      x++;
   }
   return 0;
}
'''
                        break
                    else:
                        code = code + 'int f' + str(j) + '''(int n){
    int x=0;
    f'''
                        code = code + str((j+1)%i) + '(10);\n' + '''
	if(x < n){
		x++;
	}
	return 0;
}
'''
                with open('./my_test.c', 'w') as f:
                    f.write(code)
                start = datetime.datetime.now()
                stream = os.popen('yasm -p \'EF pc=END\' ./my_test.c')
                output = stream.read()
                end = datetime.datetime.now()
                elapsed_time[kind] = elapsed_time[kind] + ((end-start).microseconds/1000)
        with open('./res.csv', 'a') as f:
            f.write(str(i) + ';' + str(elapsed_time[0]/repetitions) + ';' + str(elapsed_time[1]/repetitions) + ';' + str(elapsed_time[2]/repetitions) + '\n')

# start = datetime.datetime.now()
# elapsed_time = (end - start)

if __name__ == '__main__':
    main(sys.argv)
