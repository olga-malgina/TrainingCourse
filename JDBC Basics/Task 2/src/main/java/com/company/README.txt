This Copy Database Tool works the following way:

1. Receives name of the database and the order in which entries are to be copied (direct or reverse)
2. Creates an empty database with the default name "CopyDB"
3. Requests info on the tables of the source database and copies them to the new database
4. Requests entries from source database and at the same time copies them to the destination database

In order to speed up copying of entries (as well as the tables, as in my 11GB database there are more than 400 of them with lots of columns)
I set up fetch size, so that data was retrieved in chunks rather than everything at once. I also employed batches for insert operations not
to clog up the process as well. Still it takes quite a lot of time for my big database to be copied.

Another way of optimization might be using less complicated way to do the inserts. Here setObject is used, though, as far as I understand,
there is quite a lot going on under the hood for this method (as the compiler needs to deduce the type for example). Might be faster with
more type specific methods. But it will also make the whole application way more complicated.

