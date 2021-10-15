For this task I took the JDBC solution that I developed for Task 3 - with users, posts, likes, etc.

Not sure if I understood to a point what was needed in this task to do, but here's what I've done:
- added stored procedures in MySQL workbench (CRUD procedures and a more complicated select query)
- wrote Java code to generate a list of all the procedures in given schema
- wrote code to delete all the stored procedures in given schema
- wrote SQL scripts and code to add stored procedures to teh current schema
- wrote tests for deleting and creating the procedures

Since my application is rather small, there is not much difference in the performance of both options.
Though I do believe that creating procedures via Java code might be a better option from the code maintainability
perspective, as well as in terms of security (arguably). It is way more convenient to have all the code (including SQL scripts)
in one place and all the requests being managed the same way. It also prevents a necessary option of providing extensive access
to database for the developers who don't really need it.