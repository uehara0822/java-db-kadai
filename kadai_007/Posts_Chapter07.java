package kadai_007;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Posts_Chapter07 {
    public static void main(String[] args) {
        // データベース接続情報
        String url = "jdbc:mysql://localhost:3306/challenge_java"; // データベースURL
        String user = "root"; // データベースユーザー名
        String password = "paramix0822"; // データベースパスワード

        // データベース接続
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            System.out.println("データベース接続成功：" + connection); // 接続成功メッセージ

            // レコード追加のSQL文
            String insertSQL = "INSERT INTO posts (user_id, posted_at, post_content, likes) VALUES (?, ?, ?, ?)";

            // PreparedStatementを作成
            try (PreparedStatement statement = connection.prepareStatement(insertSQL)) {
                // データの準備
                int[] userIds = {1003, 1002, 1003, 1001, 1002};
                String[] postedAt = {"2023-02-08", "2023-02-08", "2023-02-09", "2023-02-09", "2023-02-10"};
                String[] postContents = {"昨日の夜は徹夜でした・・", "お疲れ様です！", "今日も頑張ります！", "無理は禁物ですよ！", "明日から連休ですね！"};
                int[] likes = {13, 12, 18, 17, 20};

                // レコードの追加
                System.out.println("レコードの追加を実行します");
                for (int i = 0; i < userIds.length; i++) {
                    statement.setInt(1, userIds[i]);
                    statement.setString(2, postedAt[i]);
                    statement.setString(3, postContents[i]);
                    statement.setInt(4, likes[i]);

                    statement.executeUpdate(); // レコードの追加を実行
                }

                System.out.println("5件のレコードが追加されました"); // 追加成功メッセージ
            }

            // 指定されたユーザーIDが1002の投稿を検索し、投稿日時、投稿内容、いいね数のカラムを取得して表示
            String selectSQL = "SELECT posted_at, post_content, likes FROM posts WHERE user_id = ?";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectSQL)) {
                int userId = 1002;
                selectStatement.setInt(1, userId);

                ResultSet resultSet = selectStatement.executeQuery();

                System.out.println("ユーザーIDが1002の投稿を検索しました：");
                int count = 1;
                while (resultSet.next()) {
                    String postedAt = resultSet.getString("posted_at");
                    String postContent = resultSet.getString("post_content");
                    int likes = resultSet.getInt("likes");

                    System.out.println(count + "件目：投稿日時=" + postedAt + "／投稿内容=" + postContent + "／いいね数=" + likes);
                    count++;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // エラーが発生した場合はスタックトレースを出力
        }
    }
}
