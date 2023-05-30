DELETE FROM LENDINGS;
DELETE FROM BOOKS;
DELETE FROM BOOK_NAMES;

INSERT INTO BOOK_NAMES(ID,ACTIVE,AUTHOR,CREATED_AT,DETAIL,IMG,PUBLISHER,TITLE,UPDATED_AT,GENRE_ID)
 VALUES(
    1,
    TRUE,
    '中山 清喬
    国本 大悟',
    CURRENT_TIMESTAMP,
    '本書ではJavaでドラクエ風RPGの制作することをテーマとすることで、
    読者に楽しそうなイメージを持ってもらい、途中で挫折せずにJavaを学べるよう配慮すると共に、肝心のオブジェクト指向についてもRPG風のイラストを多用して直感的に分かりやすく理解できるよう工夫しました。',
    'Javasukkiri.png',
    'インプレス',
    'スッキリわかるJava入門',
    CURRENT_TIMESTAMP,
    2),
    (
    2,
    TRUE,
    '山田 祥寛',
    CURRENT_TIMESTAMP,
    '標準教科書”が完全書き下ろしで11年ぶり新登場！
    Javaプログラミングに必要な知識・概念・機能を体系的かつ網羅的に習得！',
    'dokusyuJava.jpg',
    '翔泳社',
    '独習Java 新版',
    CURRENT_TIMESTAMP,
    2),
    (
    3,
    TRUE,
    '株式会社NTTデータ',
    CURRENT_TIMESTAMP,
    'Spring Frameworkの基礎から開発時の指針まで！
    定番OSSフレームワークによるJavaシステム開発の入門書！',
    'springbootettei.jpg',
    '翔泳社',
    'Spring徹底入門',
    CURRENT_TIMESTAMP,
    2),
    (
    4,
    TRUE,
    '原田 けいと
    竹田 甘地
    Robert Segawa',
    CURRENT_TIMESTAMP,
    '本書は、Spring Boot 2.7（+ 3.0 両対応）の入門書です。
    Spring Bootアプリケーションをいくつか作成しながら、主要な機能について解説しています。ご興味がありましたら、是非一読ください。',
    'springboot2nyumon.jpg',
    'Independently published',
    'Spring Boot 2 入門',
    CURRENT_TIMESTAMP,
    2),
    (
    5,
    TRUE,
    '志賀 澄人',
    CURRENT_TIMESTAMP,
    'オラクル社の資格試験「Oracle Certified Java Programmer, Silver SE 11 認定資格(試験番号: 1Z0-815-JPN)に完全対応!
新しい試験範囲を完全に網羅。教科書テキストがなくても、この問題集1冊でとても丁寧に解説しているので、ラムダ式やモジュールについてもすんなりと理解できます。',
    'javasilverse11.jpg',
    'インプレス',
    '徹底攻略Java SE 11 Silver問題集
    [1Z0-815]対応',
    CURRENT_TIMESTAMP,
    2);
