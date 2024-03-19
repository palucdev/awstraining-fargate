# awstraining-backend
This repository holds reference Spring Boot project that can be deployed to AWS

# Run locally
To run this application locally, please first call ```docker-compose up``` in ```/local/assembly-local``` directory.

This will set up the following components:
* DynamoDB
  * With 'Measurements' table holding measurements for devices 
  * http://localhost:8000
* DynamoDB Admin Panel
  * http://localhost:8001
* Filebeat
  * It will load Spring Boot logs from file and redirect them to Elasticsearch
* Kibana
  * It will allow visual access to application logs 
  * http://localhost:5601
* Prometheus
  * It will allow querying application metrics
  * http://localhost:9090
* Grafana
  * It will allow dashboards creation
  * http://localhost:3003
* Elasticsearch
  * It will allow indexing and saving logs for later visual access via Kibana
  * http://localhost:9200

DynamoDB will be populated with test measurement data.

Then, please configure ```application.yml```:
```yml
aws:
  region: eu-central-1
  dynamodb:
    endpoint: http://localhost:8000
    accessKey: dummyAccess
    secretKey: dummySecret
```

We have to point to our local DynamoDB instance. Access and secret keys must be set to any values, they simply cannot 
stay empty.

Finally, simply run Application in IntelliJ with 'Run' button.

# Preparation to the deployment
To deploy infrastructure to your sandbox account please first fork our base repository.
To do it, go to:
* https://github.com/Alegres/awstraining-backend

and click on Fork button and then (+) Create new fork.

After forking repository to your account, please clone it to your local machine and search for all occurrences of:
* <<ACCOUNT_ID>>

This is base AWS account id that we use for the base repository.
You must replace this with your own account id in all files.

Push changes to your remote repository.

Then you should create a new profile in ```C:\Users\YOURUSER\.aws\credentials``` and set credentials to your account:
```
[backend-test]
aws_access_key_id = YOUR_ACCESS_KEY_ID
aws_secret_access_key = YOU_SECRET_ACCESS_KEY
```

**DO NOT USER ROOT USER CREDENTIALS!** Instead, create admin user in IAM, assign him **AdministratorAccess** policy
and generate credentials for this non-root user.

Then please run bash (e.g. Git Bash), and go to . ``/aws-infrastructure/terraform``` directory.
Set 'RANDOM_STRING' environmental variable. This random string should be some random value. It is important to come up 
with an unique value, as this will affect the name of the Terraform state bucket that will be created, thus it must 
be unique globally. Please also do not make it too long.
Here is example
```
export RANDOM_STRING="dakj18aad88"
```

Please again push changes to your remote repository.

# Deploying AWS infrastructure
To run Terraform you first need to install it on your local machine.
You need **terraform_1.4.6** or higher version.

Now you can run a script to set up a new AWS environment (still in ```/aws-infrastructure/terraform``` directory):
```
./setup_new_region.sh w2.sh backend-test eu-central-1 emea apply -auto-approve
```

Terraform should automatically approve all changes and create all required resources one-by-one.
In case of errors, please correct them, delete from setup_new_region.sh lines that has already been executed and run 
the script again.

Then you should go to **GitHub -> Your fork repo -> Settings -> Secrets and variables**
and create two repository secrets:
* BACKEND_EMEA_TEST_AWS_KEY
* BACKEND_EMEA_TEST_AWS_SECRET

and set accordingly **AWS_KEY** and **AWS_SECRET**, same as in ```..\.aws\credentials```.

It is all what we have to do for secrets in GitHub.

Now go to AWS Secret Manager, copy arn of created Secret and adjust it in your code. Then enter some dummy values for created Secret.

# Build & Deploy
When you are done with setting up the infrastructure, please go to your fork repository, open **Actions** tab and run
**Multibranch pipeline** on the main branch.

This branch will build Docker image, push it to ECR and deploy application to ECS Fargate.
After it has finished, you should go to your AWS account, open EC2 Load Balancers page and find
backend application load balancer.

Please then copy DNS of this load balancer and feel free to run test curls.
Example:
```
curl --location 'http://backend-lb-672995306.eu-central-1.elb.amazonaws.com/device/v1/test' \
--header 'Authorization: Basic dGVzdFVzZXI6d2VsdA=='
```

```
curl --location 'http://backend-lb-672995306.eu-central-1.elb.amazonaws.com/device/v1/test' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic dGVzdFVzZXI6d2VsdA==' \
--data '{
    "type": "testing",
    "value": -510.190
}'
```

User is **testUser** and password is **welt**.

# Destroying AWS infrastructure
Stop all running tasks.

Delete images from AWS ECR and delete secrets from AWS Secret Manager.

Run a script to destroy a new AWS environment (in ```/aws-infrastructure/terraform``` directory):
```
./setup_new_region.sh backend-test eu-central-1 emea destroy -auto-approve
```

Terraform should automatically approve all changes and delete all existing resources one-by-one.
In case of errors, please correct them, delete from setup_new_region.sh lines that has already been executed and run 
the script again.

Check IAM, Cloudwatch Logs, S3 buckets if everything was deleted
