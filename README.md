Mirriad Test
============

This application is a solution to a Mirriad test for those applying to the position of **Software Engineer**. The test consist on resolving a generic problem following the following guideline:

> Supermarket Check Out
> ---------------------

> Implement a Supermarket checkout that calculates the total price of a number of items.
> 
> Some items have multiple prices based on price rules such as:
> 
> - buy 3 (equals) items and pay for 2
> - buy 2 (equals) items for a special price
> - buy 3 (in a set of items) and the cheapest is free
> - for each N (equals) items X, you get K items Y for free
> 
> 
> The output required is the receipt with the actual price of every item and the grand total.
> 
> You should not spend more than a few hours on this problem.
> 
> You may choose any means of accepting input and producing output, including the use of a test harness.
> 
> The code should be simple and flexible so that any new rule should be added with the minimum effort.

Run Application
----------------
To run the application follow the next steps:
 
 - Open a terminal and change to the project root directory
 - Run the command `gradlew.bat installApp`
 - Run the command `./build/install/mirriad/bin/mirriad.bat`
 - Now perform a post call to the endpoint `http://localhost://8080/checkout` with a json body, for instance:
 
 
    curl -X POST -d '{
        "items":[{
            "id":100,
            "name":"Apple",
            "unitPrice":1.25,
            "quantity":7
            
        },{
            "id":200,
            "name":"Orange",
            "unitPrice":0.17,
            "quantity":2
            
        }]
    }' 'http://localhost:8080/checkout'

*For more information please contact [Ricardo Villanueva](villanueva.ricardo@gmail.com)*