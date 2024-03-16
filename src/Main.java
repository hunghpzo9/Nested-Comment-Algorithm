import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        String input;
        Scanner scanner = new Scanner(System.in);
        List<Comment> comments = new ArrayList<>();
        do {
            Comment comment = new Comment();
            int right = 0;
            System.out.print("Enter postId: ");
            int postId = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter id comment: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter contents: ");
            String contents = scanner.nextLine();

            System.out.print("Enter parentCommentId: ");
            int parentCommentId = scanner.nextInt();

            if (parentCommentId < 0) {
                right = 1;
                comment.setRootCommentId(id);

            } else {
                //replay comment
                Comment parentComment = getCommentById(comments, parentCommentId);
                if (parentComment != null) {
                    right = parentComment.getRight();
                    comment.setRootCommentId(parentComment.getRootCommentId());
                    getCommentsToUpdate(comments, parentComment.getRootCommentId(), right);
                }
                //Update all comment where (right >= right or left > right and rootCommandId = parent.getRoot) value: currentRight+2,currentlLeft+2

            }


            comment.setId(id);
            comment.setPostId(postId);
            comment.setContents(contents);
            comment.setParentCommentId(parentCommentId);
            comment.setLeft(right);
            comment.setRight(right + 1);
            comments.add(comment);

            comments.stream().forEach(c -> System.out.println(c.toString()));

            scanner.nextLine();
            System.out.print("Enter 'ok' to stop, or press Enter to continue: ");
            input = scanner.nextLine();

        } while (!input.equalsIgnoreCase("ok"));
        scanner.close();

    }
    private static Comment getCommentById(List<Comment> comments, int commentId) {
        return comments.stream()
                .filter(comment1 -> comment1.getId() == commentId)
                .findFirst()
                .orElse(null);
    }

    private static List<Comment> getCommentsToUpdate(List<Comment> comments, int rootCommentId, int right) {
        return comments.stream()
                .filter(comment -> comment.getRootCommentId() == rootCommentId)
                .filter(comment -> comment.getRight() >= right || comment.getLeft() > right)
                .peek(comment -> {
                    if(comment.getLeft()>right)
                       comment.setLeft(comment.getLeft()+2);
                }).peek(comment -> comment.setRight(comment.getRight()+2))
                .collect(Collectors.toList());
    }
}