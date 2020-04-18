# Gitlet Design Document

**Name**: Jessica Yang 

## Classes and Data Structures
init
in the current directory, creates a new gitlet version control system 
puts 1 commit in, the starting commit. 

add
tracks the file for upcoming commit, indicating that the file is to be included in the next commit. 
If the file was marked for removal, unmark for removal.
if the file has not been altered, then do not commit  

commit
saves snapshot of current project, the files staged for commit and takes over past commit.
the past commit are inherited from the previous commit, and the only difference is the added files (stored in a document)

remove
untracks file so it will not be uploaded in the next commit
also can remove a branch if a branch name is passed in 

log

displays the commit info from the most recent to the earliest commit 

find

will print out the commit id with the given commit message

status

displays files are staged for commit or removal. also displays what branches currently exist 
marks current branch with a *

checkout 

restoring a previous version of the file to be the head

reset

reset all files to a certain version with given ID 
head moves to that node.


merge

merge files from head of given branch to current branch 

branch 

creates a new branch with the given name, points it at the current head node


main

will call another class depending on the command 



## Algorithms
init 

if there already exists a this, system prints "A gitlet system already exists in the current directory"

add

if the file does not exist, print error 
file to keep track of all files being staged, and resets with each commit 
file to keep track of all staged for removal 

commit 
will add files in the staging area, which must be clear at the end of a commit

if no files staged, error message
commit must have message
it moves the head pointer to the new node, and a new node is added

log

the information displayed will be 
1. commit ID
2. commit message
3. time of commit
most recent commit at the top! 
most likely will use a tostring method 
*history is immutable


find 

if no commit exists, print error
if there are multiple commits, print all of them

remove 

if the file is not staged or tracked, print error "no reason to remove the file"

checkout

if file is current branch print error bc no checkout needed
if no file exists, print error
usages
1. file name
version of file as it exists currently (in the head commit), and puts it in the working directory
overwriting file if its already there 
2. branch name
takes all the files and puts them in working directory
given branch will now be considered head 
3. commit ID and file name
same as 1

** this is dangerous as it overwrites 

merge

if it does not exist, print error
cannot merge with itself

branch
creates a new pointer that can be switched using checkout 



## Persistence

things to keep track of:
1. files for add, reset at each commit. similary, a file for removal is needed too 
2. the current branches 
3. the commit tree history
4. linked list "head"
5. a marker for whether or not the file has been modified
