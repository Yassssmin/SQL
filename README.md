Run project:

```
 docker-compose up -d
```

```
 java -jar artifacts/app-deadline.jar -P:jdbc.url=jdbc:mysql://localhost:3306/app -P:jdbc.user=user -P:jdbc.password=feada149cb8ff54e
```

Stop project:

```
docker-compose down -v
```