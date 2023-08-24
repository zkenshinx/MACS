.data
 newline: .asciiz "\n"

.text
.globl start

start:
	jal 	readint
	move 	$t3, $v0	# read a -.> t3
	jal 	readint
	move 	$t4, $v0	# read b - > t4
	
	move    $a0, $t3
	move    $a1, $t4
	jal 	gcd             # Call gcd function
	
	move 	$t0, $v0        # t0-shi weria gcd
	
	# a / gcd
	div     $t3, $t0
	mflo 	$a0
    	li 	$v0, 1
   	syscall
   	
   	# daprinte axali xazi
	la $a0, newline
	li $v0, 4
	syscall
	
   	# b / gcd
   	div     $t4, $t0
   	mflo    $a0
   	li      $v0, 1
   	syscall
	
	li  $v0, 10
    	syscall 
readint:
	li 	$v0, 5
	syscall
	jr 	$ra
gcd:
	div  	$a0, $a1
	mfhi    $t1
	beq 	$a1, 0, loopend
	move 	$a0, $a1
	move    $a1, $t1
	j       gcd
loopend:
	move 	$v0, $a0
	jr 	$ra
