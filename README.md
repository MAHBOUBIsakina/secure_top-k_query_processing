# Secure TOP-K Query Processing
Nowadays, cloud data outsourcing provides users and companies with powerful capabilities to store and process their data in third-party data centers. However, when a user stores her data in a public cloud, she loses the physical access control to the data. Thus, potentially sensitive data gets at risk of security attacks, e.g., from the employees of the cloud provider. According to a recent studies, security attacks are one of the main concerns for the cloud users. 

One solution for protecting the user data against attacks is to encrypt the data before sending them to the cloud servers. Then, the challenge is to answer user queries over encrypted data. A naive solution for answering queries is to retrieve the encrypted database from the cloud to the client, decrypt it, and then evaluate the query over plaintext (non encrypted) data. This solution is not practical, in particular for large databases. 

In our study, we are interested in processing top-k queries over encrypted data. These queries have attracted much attention in several areas of information technology such as sensor networks, information retrieval, data stream management systems, spatial data analysis, and graph databases. A top-k query allows the user to specify a number k, and the system returns the k tuples which are most relevant to the query. The relevance degree of tuples to the query is determined by a scoring function.  

There have been many different approaches proposed for processing top-k queries over plaintext data. One of the best known approaches is TA that works on sorted lists of attribute values. TA can find efficiently the top-k results because of a smart strategy for deciding when to stop reading the database. However, TA and all other efficient top-k approaches developed so far assume the existence of local scores of the data items (i.e. their attribute values) in plaintext, and there is no efficient solution capable of evaluating efficiently top-k queries over encrypted data.

When we think about top-k query processing on encrypted data, the first idea that comes to mind is the utilization of a fully homomorphic encryption cryptosystem, which allows one to do arithmetic operations over encrypted data. Using this type of encryption allows to compute the overall score of data items over encrypted data. However, existing fully homomorphic encryption methods are very expensive in terms of encryption and decryption time. In addition, they do not allow to compare the encrypted data, and to find the top-k results. 

To solve this problem, we propose a new approach, called BuckTop that efficiently evaluates top-k queries over encrypted data. BuckTop uses the bucketization technique to partition the encrypted data into a set of buckets before sending them to the server. Our work includes the following contributions:

- A top-k query processing algorithm that works on the encrypted data of the buckets, and returns a set, which is proved to contain the encrypted data corresponding to the top-k results. 

- An efficient filtering algorithm that filters in the server significantly the false positives included in the set returned by the top-k query processing algorithm. We prove theoretically the correctness of the filtering algorithm. 

- A novel approach to obfuscate the boundaries of the buckets that contain the data scores, thus increasing the security of the buckets.

We implemented our approach, and compared its response time over encrypted data with a base algorithm and also with TA over original (plaintext) data. The experimental results show excellent performance gains for BuckTop. For example, the results show that the response time of BuckTop over encrypted data is close to TA over plaintext data. Over some databases, the response time of BuckTop is even better than TA. The results also illustrate that more than 99.9 \% of the false positives can be eliminated in the server by BuckTop's filtering algorithm.  

# Parameters configuration
The parameters are modifiable by changing their values in proposition1main.java class.
# Code execution 
Only run proposition1main.java class after changing the parameters.
# Paper
<a href="https://github.com/MAHBOUBIsakina/secure_top-k_query_processing/blob/master/VLDB%20Paper/Secure_Topk_Long_Version.pdf">S. Mahboubi, R. Akbarinia, and P. Valduriez. Rank query processing over outsourced encrypted data. Under review, 2017.</a>


