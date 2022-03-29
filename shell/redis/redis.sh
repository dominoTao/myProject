docker run -d -p 6379:6379 --name myredis -v /usr/local/docker/redis/redis.conf:/etc/redis/redis.conf -v /usr/local/docker/redis/data:/data redis redis-server /usr/local/docker/redis/redis.conf

#docker build --pull --rm --tag myredisimage:v1 -f ./Dockerfile .
docker build --pull --rm --tag my_redis_image:v1 .
#[root@192 redis]# docker build --pull --rm --tag my_redis_image:v1 .
#	Sending build context to Docker daemon  97.28kB
#	Step 1/3 : FROM redis
#	latest: Pulling from library/redis
#	Digest: sha256:394dc1b4287dd99ca928af9a4d4892feed7e5563be424e4676a69bc6e5aa0ab4
#	Status: Image is up to date for redis:latest
#	 ---> 87c26977fd90
#	Step 2/3 : COPY redis.conf /usr/local/docker/redis/redis.conf
#	 ---> dc5015a85fe6
#	Step 3/3 : CMD [ "redis-server", "/usr/local/docker/redis/redis.conf" ]
#	 ---> Running in a207b9643aec
#	Removing intermediate container a207b9643aec
#	 ---> e8b77a9ace42
#	Successfully built e8b77a9ace42
#	Successfully tagged my_redis_image:v1

docker run -d -p 6379:6379 --name myredis -v /usr/local/docker/redis/data:/data my_redis_image:v1

