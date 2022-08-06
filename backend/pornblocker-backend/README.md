# Pornblocker Backend


## プロジェクトを起動する

```shell
./gradlew bootRun
```

## Gradleで依存関係をインストールする


```shell
gradle clean build install
```

[gradle-mvn.md](https://gist.github.com/diegopacheco/a95fcd991825a607fffb)

### 依存関係をゼロからインストールし直す場合

```shell
./gradlew build --refresh-dependencies
```

[How can I force gradle to redownload dependencies?](https://stackoverflow.com/questions/13565082/how-can-i-force-gradle-to-redownload-dependencies)


依存関係を追加した後は、Gradleプロジェクトを再ロードする。

[Gradle dependencies](https://www.jetbrains.com/idea/guide/tutorials/working-with-gradle/gradle-dependencies/)


## H2Databaseのセットアップ

[Spring Boot With H2 Database](https://www.baeldung.com/spring-boot-h2-database)