![](https://img.shields.io/badge/licenes-MIT-brightgreen.svg)
![](https://img.shields.io/badge/jdk-1.8+-brightgreen.svg)

## Project structure diagram

<img src="http://www.mars-framework.com/img/jgt.png" width="600px;"/>
<br/><br/>

##### The SQL Center is under development and is temporarily unavailable. Please look forward to it.

## Declarative API Programming (DAP)

According to the traditional development method, if you want to develop a back-end interface, you need to divide into the following three steps.

1. Create a controller
2. Create a service
3. Create dao (even create xml to store sql)

However, we write an interface, the focus should be on the business logic, which means that our focus should be in the second step, but usually the repetitive workload of the first and third steps makes us feel suffocated, so I After sorting out a bit, I developed a new gameplay. This new gameplay is called a declarative API.

1. Write business logic
2. Declare an API to the front end
3. Associate the API with the business logic

So we are playing like this

## Writing business logic

```
@MarsBean("testService")
Public class TestService {

    <The type of data to return> selectListForName(TestDTO testDTO){
        // The first step is to query the required data from the xx table according to the parameters in testDTO.
        // The second step is to operate the xx2 table based on the detected data.
        // The third step summarizes the results of the first two steps and performs the xxx operation.
        
        Return data (return directly, it will automatically become json);
    }
}
```
## Declare an API interface

```
@MarsApi
Public interface TestApi {

    Object selectList(TestDTO testDTO);
}
```

## Associating api with business logic

```
@MarsApi
Public interface TestApi {

    @MarsReference(beanName = "testService",refName = "selectListForName")
    Object selectList(TestDTO testDTO);
}
```

The core of this idea is that the back end is regarded as an independent entity, not for the service front end. The back end writes the business logic of the back end. If the front end needs data, then we will open a door to him.

The benefits of doing this can also be dilated

- Can be linked to different business logic by changing the configuration of MarsReference
- If the front end does not need this interface, it is fine to delete it directly, because this is just an abstract method.
- The back end focuses on the business logic, so you don't need to think about interacting with the front end. It's good to open the door when the front end needs it.

## How do you do it next?

Seeing this, everyone will have doubts, how should the front end call the api, how does the backend operate the database? This requires you to move your fingers and go to my official website to find out.