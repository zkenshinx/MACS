.data
  dp_array: .space 1500 #  Enough if the input words are max 15 lenght0
  first_string: .space 16
  second_string: .space 16
  newline: .asciiz "\n"
  space: .asciiz " "
.text
.globl main
main:    
	# Read first string
	la $a0, first_string
	jal readstring
	# Read second string
	la $a0, second_string
	jal readstring
	li $t7, 0 # THIS IS WHERE THE LONGEST COMMON SUBSEQUENCE LENGTH WILL BE STORED
	li $t0, 1
	outer_loop:
		# load current char address in t2
		la $t2, first_string
		add $t2, $t2, $t0
		sub $t2, $t2, 1
		# load that char
  		lb $t3, ($t2)
  		# loop condiition
  		beq $t3, 10 outer_end
  		
  		li $t1, 1
  		inner_loop:
  			# load current char addres in t2
  			la $t2, second_string
  			add $t2, $t2, $t1
			sub $t2, $t2, 1
			# load that char
			lb $t4, ($t2)
			# loop condition
			beq $t4, 10, inner_end
			
			# so far $t3 has char value for first_string
			# and $t4 has char value for second_string
			bne $t3, $t4, not_equal
			equal:					
				move $a0, $t0
				sub  $a0, $a0, 1
				move $a1, $t1
				sub  $a1, $a1, 1
				jal get_dp_address
				move $t5, $v0
				lw   $t5, ($t5)
				add  $t5, $t5, 1 # dp[i - 1][j - 1] + 1
				
				# get dp[i][j] address
				move $a0, $t0
				move $a1, $t1
				jal get_dp_address
				
				sw $t5, ($v0)
				
				# result = max(result, dp[i][])
				move $a0, $t7
				move $a1, $t5
				jal max_of_two
				move $t7, $v0
				j if_end
			not_equal:
				move $a0, $t0
				sub  $a0, $a0, 1
				move $a1, $t1
				jal get_dp_address
				move $t5, $v0  
				lw   $t5, ($t5) # t5 has value of dp[i][j - 1]
				
				move $a0, $t0
				move $a1, $t1
				sub  $a1, $a1, 1
				jal get_dp_address
				move $t6, $v0  
				lw   $t6, ($t6) # t6 has value of dp[i - 1][j]
				
				# get max of these 2
				move $a0, $t5
				move $a1, $t6
				jal  max_of_two
				move $t5, $v0
				
				# get dp[i][j] address
				move $a0, $t0
				move $a1, $t1
				jal get_dp_address
				
				sw $t5, ($v0)
			if_end:		
			addiu $t1, $t1, 1
			j inner_loop
		inner_end:  			
  		addiu $t0, $t0, 1
  		j outer_loop
  	outer_end:
  	# print the result
  	move $a0, $t7
  	jal printint
  	jal exit
max_of_two:
	# save t0
	move $s0, $t0
	ble $a0, $a1 second_is_bigger
	first_is_bigger:
		move $t0, $a0
		j max_of_two_end
	second_is_bigger:
		move $t0, $a1
	max_of_two_end:
	move $v0, $t0
	# restore $t0
	move $t0, $s0
	jr   $ra
get_dp_address:
	# store t0 and t1
	move $s0, $t0
	move $s1, $t1
	
	# calculates adders of dp[i + 16 * j]
	move $t0, $a0
	mul  $t0, $t0, 4 
	move $t1, $a1
	mul  $t1, $t1, 64   # 16 * j
	add  $t0, $t0, $t1  # i + 16 * j
	la   $t1, dp_array  
	add  $t1, $t1, $t0  # address of dp[i + 16 * j]
	move $v0, $t1
	
	# get back t0 and t1
	move $t0, $s0
	move $t1, $s1
	jr $ra
exit:
    	li $v0, 10          # exit system call code
    	syscall             # exit program
readstring:
	# in a0 is written the buffer address
	li $v0, 8           # 8 is syscall number for reading string
	li $a1, 17          # Maximum string length including '\n' and '\0'
	syscall
	jr $ra
printint:
	# print the int
	li $v0, 1
	syscall
	# print new line
	la $a0, newline
	li $v0, 4
	syscall
	jr $ra
readint:
	li $v0, 5
	syscall
	jr $ra