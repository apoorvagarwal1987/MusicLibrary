
Compile the project
===============


1. Project needs JAR **json-simple-1.1.1**.  This JAR could be downloaded from [json-simple-1.1.1](https://code.google.com/archive/p/json-simple/downloads).
 
 1. Copy the jar into the project directory.
 
 1. Add the jar in the project path and compile all the class files of the project using the following command.
>       find . -name "*.java" > sources_list.txt

>       javac -cp ../json-simple-1.1.1.jar @sources_list.txt

Execute the project
===============

1. Change the current directory to the src.
1. Provide following arguments to the command line
    1. Input_file_name.json
    1. Change_file_name.json
    1. Operation_to_be_perform 0: ADD_PLAYLIST, 1: REMOVE_PLAYLIST, 2: ADD_SONGS_TO_PLAYLIST   
1. An example command would look like 
>         java -cp .:../json-simple-1.1.1.jar operation/Operation /Users/apoorvagarwal/IdeaProjects/MusicLibrary/resources/mixtape-data.json /Users/apoorvagarwal/IdeaProjects/MusicLibrary/resources/add_songs_to_playlist.json 0


Scaling the project
===============

To support big files that would mean a lot more data to be processed. This could be achieved by storing these data in some form of a database. The database is needed as the file size is bigger storing all these data in memory would not be a possible option for scaling and serving several change requests. Having some sort of database would help in managing the correctness of the changes requested in the change_file. 

### Database Design

The choice of the database in this scenario could be a NoSQL database. 
The decision of the NoSQL database is made based on the structure of the data. The records that are getting stored are as a Key-Value pair.
System requirement doesn’t entail any relational query, as we are not doing any kind of analytical query over these records so we wouldn’t be joining these tables. As NoSQL databases are not performant in joining the tables.
Another benefit of choosing the NoSQL database is that we can partition the database on the keys of the records and provide us with an option of horizontal scaling. 

The AWS technology DynamoDB could be a good fit for storing these records provided in the input_file. The changes that are made by the change_file would be later applied to the records of the DynamoDB.

### Chunker
This component of the system would be responsible to break down the large files into smaller batches and apply the changes in the database. The component would be making temporary tables in the DynamoDB to create the Users, Songs, and Playlist table for each request. This component would also be responsible to apply the changes from the change request file.

### Aggregator
This component sitting on top of the Chunker called Aggregator would be collecting the response from each chunk request and when all the chunk requests for the particular requests have been processed, component DataDumper would be triggered. 

### DataDumper
The component DataDumper would be reading all the records from those 3 tables for the particular request_id and translate the data into either CSV or JSON and store them in the S3(Object storage). After the S3 object is created all 3 tables related to the request would be deleted.

### User Notification
After the DataDumper is done creating the S3 file it would send the notification to the user with the S3 Object link that they could use to download the file. This could be easily done with the AWS Transfer feature that could transfer this file using FTP or SFTP(securely).

### Error Recovery
While processing these individual chunks if in case any failure happened chunker would be responsible to relay that information back to the Aggregator. At this point as this is a batch processing aggregator would continue to apply all the changes in the database. To inform the users that a failure in the batch happened aggregator while triggering the DataDumper would inform them about these failed requests. DataDumper while creating the dump of the tables would also create the error_manifest file with the details of such failures. In this event, it would not delete the 3 tables. This is done for users to just retry those failed change_requests again and the system doesn’t need to recreate all these database tables. 

### Clean Up
A job would be created to clean up the tables in the DynamoDB that are still there and are like 3 months old (the arbitrary number could be changed as per the requirement).






