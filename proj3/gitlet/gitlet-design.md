# Gitlet Design Document

**Name**: Jessica Yang 

## Classes and Data Structures
init
creates a new gitlet version control system 
puts 1 commit in, the starting commit 

add
tracks the file for upcoming commit 

commit
saves snapshot of current project, the files staged for commit and takes over past commit  

remove
untracks file so it will not be uploaded in the next commit

log

displays the commit info from the most recent to the earliest commit 

find

will print out the commit id with the given commit message

status

displays files are staged for commit or removal. also displays what branches currently exist 

checkout 

restoring a previous version of the file to be the head

reset

reset all files to a certain version with given ID 
head moves to that node.

merge

merge files from head of given branch to current branch 

main

will call another class depending on the command 



## Algorithms
init 

if there already exists a this, system prints error

add

if the file does not exist, print error 
file to keep track of all files being staged, and resets with each commit 
file to keep track of all staged for removal 

commit 
if no files staged, error message
commit must have message

find 

if no commit exists, print error

checkout

if file is current branch print error bc no checkout needed
if no file exists, print error

merge

if it does not exist, print error
cannot merge with itself




## Persistence

things to keep track of:
1. files for add, reset at each commit
2. the current branches 
3. the commit tree history
4. linked list "head"
