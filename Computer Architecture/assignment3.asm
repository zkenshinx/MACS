.data
  set_array: .space 100 # more than 20 * 4 bytes
  set_size:  .word 0
  N:         .word 0
  newline: .asciiz "\n"
  space: .asciiz " "
  yes_string: .asciiz "YES\n"
  no_string: .asciiz "NO\n"
.text
.globl main
main:    	
	jal readint # We read N
	sw $v0, N
	# Execute loop N times
input_loop:
	# if N == 0 exit, else N -= 1
	lw $t1, N
	beqz $t1, exit
	subi $t1, $t1, 1
	sw $t1, N
	
	jal readint # read which instruction it is
	
	move $t0, $v0
	
	li $t1, 1
	beq $t0, $t1, iflabel1
	li $t1, 2
	beq $t0, $t1, iflabel2
	li $t1, 3
	beq $t0, $t1, iflabel3
	jal print_all_set_values
    	j input_loop
iflabel1:
	jal readint
	move $a0, $v0
	jal add_value_to_set
	j input_loop
iflabel2:
	jal readint
	move $a0, $v0
	jal remove_value_from_set
	j input_loop
iflabel3:
	jal readint
	move $a0, $v0
	jal check_if_set_contains
	j input_loop
    	
add_value_to_set:
    # Store ra at the top of stack
    addi $sp, $sp, -4
    sw $ra, 0($sp) 

    la $t0, set_array
    lw $t1, set_size
    
    # $t2 is loop variable
    li $t2, 0
    add_value_to_set_loop:
    	bge $t2, $t1, add_value_to_set_loop_end
    	lw $t3, ($t0) # load what value is stored at this set_array pointer
    	beq $a0, $t3, add_value_to_set_end
    	bgt $t3, $a0, add_value_to_set_loop_end
    	# increment loop variable and set_array pointer
    	addi $t2, $t2, 1
    	addi $t0, $t0, 4
    	b add_value_to_set_loop
    add_value_to_set_loop_end:
    
    # Get array base pointer
    la $t0, set_array
    # get array size
    lw $t1, set_size
    
    # $t4 = set_size - 1
    move $t4, $t1
    subi $t4, $t4, 1
    
    # Array last element pointer
    move $t3, $t4
    mul $t3, $t3, 4
    add $t0, $t0, $t3
    move_right_loop:    
    	blt $t4, $t2, move_right_loop_end
    	lw $t3, ($t0)
    	addi $t0, $t0, 4
    	sw $t3, ($t0)
    	# decrement set_array pointer
    	subi $t0, $t0, 8
    	# decrement loop variable
    	subi $t4, $t4, 1
    	b move_right_loop
    move_right_loop_end:
    
    la $t0, set_array
    mul $t2, $t2, 4
    add $t0, $t0, $t2
    sw $a0, ($t0)
    
    # increment set_size
    lw $t1, set_size
    addi $t1, $t1, 1
    sw $t1, set_size
    
    add_value_to_set_end:
    # Restore ra
    lw $ra, 0($sp)     
    addi $sp, $sp, 4  
    jr $ra
    
remove_value_from_set:
    # Store ra at the top of stack
    addi $sp, $sp, -4
    sw $ra, 0($sp) 
    
    la $t0, set_array
    lw $t1, set_size
    
    # $t2 is loop variable
    li $t2, 0
    remove_value_from_set_loop:
    	# pirobashi weria ro wasashleli ricxvi sul iarsebebs mara iyos mainc aq es xazi
    	bge $t2, $t1, remove_value_from_set_loop_end
    	lw $t3, ($t0) # load what value is stored at this set_array pointer
    	beq $a0, $t3, remove_value_from_set_loop_end
    	# increment loop variable and set_array pointer
    	addi $t2, $t2, 1
    	addi $t0, $t0, 4
    	b remove_value_from_set_loop
    remove_value_from_set_loop_end:
    
    # decrement set_size
    subi $t1, $t1, 1
    sw $t1, set_size
    move_left_loop:
    	bge $t2, $t1, move_left_loop_end
    	addi $t0, $t0, 4
    	lw $t3, ($t0)
    	subi $t0, $t0, 4
    	sw $t3, ($t0)
    	add $t0, $t0, 4
    	addi $t2, $t2, 1
    	b move_left_loop
    move_left_loop_end:
    
    # Restore ra
    lw $ra, 0($sp)     
    addi $sp, $sp, 4  
    jr $ra
    
check_if_set_contains:
    # Store ra at the top of stack
    addi $sp, $sp, -4
    sw $ra, 0($sp) 
    
    la $t0, set_array
    lw $t1, set_size
    
    # $t2 is loop variable
    li $t2, 0
    check_if_set_contains_loop:
    	bge $t2, $t1, contains_no
    	lw $t3, ($t0) # load what value is stored at this set_array pointer
    	beq $a0, $t3, contains_yes
    	# increment loop variable and set_array pointer
    	addi $t2, $t2, 1
    	addi $t0, $t0, 4
    	b check_if_set_contains_loop
    contains_yes:
    la $a0, yes_string
    b print_result
    contains_no:
    la $a0, no_string
    print_result:
    li $v0, 4
    syscall
     # Restore ra
    lw $ra, 0($sp)     
    addi $sp, $sp, 4  
    jr $ra
    
print_all_set_values:
    # Store ra at the top of stack
    addi $sp, $sp, -4
    sw $ra, 0($sp) 
    
    la $t0, set_array
    lw $t1, set_size
    
    li $t2, 0
    print_all_set_values_loop:
    	bge $t2, $t1, print_all_set_values_loop_end 
    	lw $a0, ($t0)
    	jal printint
    	addi $t0, $t0, 4
    	addi $t2, $t2, 1
    	b print_all_set_values_loop
    print_all_set_values_loop_end:
    # print new line
    la $a0, newline 
    li $v0, 4
    syscall
     # Restore ra
    lw $ra, 0($sp)     
    addi $sp, $sp, 4  
    jr $ra
    
exit:
    	li $v0, 10          # exit system call code
    	syscall             # exit program
printint:
	# print the int
	li $v0, 1
	syscall
	# print space
	la $a0, space
	li $v0, 4
	syscall
	jr $ra
printintln:
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
	
