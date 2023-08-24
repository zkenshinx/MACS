.data
.text
.globl start

start:
	jal 	readint
	move 	$t1, $v0	# Let's read a
	jal 	readint
	move 	$t2, $v0	# Let's read b
	jal 	readint        
	move	$t3, $v0        # Let's read c
	

	sub 	$t1, $t1, $t3   # a -= c
	sub     $t2, $t2, $t3   # b -= c
	
	abs 	$t1, $t1        # a = abs(a)
	abs 	$t2, $t2        # b = abs(b)

	# if $t1 <= $t2 $t4 = 1 else 2
	bgt 	$t1, $t2, else	
	li      $t4, 1          # first is closer
	b       afterelse
else:
	li      $t4, 2          # second is closer
afterelse:		
	li 	$v0, 1
	move	$a0, $t4
	syscall
	
	li  $v0, 10
    	syscall 
readint:
	li 	$v0, 5
	syscall
	jr 	$ra