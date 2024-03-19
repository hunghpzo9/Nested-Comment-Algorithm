import java.util.ArrayList;
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
            System.out.print("Enter option 1-Add 2-Delete: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            if(option == 1) {
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
                        updateAllCommentWhenAdd(comments, parentComment.getRootCommentId(), right);
                    }
                }

                comment.setId(id);
                comment.setPostId(postId);
                comment.setContents(contents);
                comment.setParentCommentId(parentCommentId);
                comment.setLeft(right);
                comment.setRight(right + 1);
                comment.setStatus(1);
                comments.add(comment);
            }else{
                System.out.print("Enter delete Comment Id: ");
                int commentId = scanner.nextInt();
                scanner.nextLine();
                Comment deletedComment = getCommentById(comments, commentId);
                if(deletedComment.getRootCommentId() == commentId){
                    //If root then delet all
                    comments.stream().forEach(c -> c.setStatus(0));
                }else{
                    updateAllCommentWhenDeleted(comments,deletedComment.getRootCommentId(),deletedComment.getLeft(),deletedComment.getRight());
                }

            }


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

    private static void updateAllCommentWhenAdd(List<Comment> comments, int rootCommentId, int right) {
         comments.stream()
                .filter(comment -> comment.getRootCommentId() == rootCommentId)
                .filter(comment -> comment.getRight() >= right || comment.getLeft() > right)
                .peek(comment -> {
                    if(comment.getLeft()>right)
                       comment.setLeft(comment.getLeft()+2);
                }).peek(comment -> comment.setRight(comment.getRight()+2))
                .collect(Collectors.toList());
    }
    private static void updateAllCommentWhenDeleted(List<Comment> comments, int rootCommentId,int left, int right) {
        int deletedNum = right-left + 1 ;
         comments.stream()
                .filter(comment -> comment.getRootCommentId() == rootCommentId)
                .filter(comment -> comment.getRight() >= right || (comment.getLeft() > right && comment.getRight() < right))
                .peek(comment -> {
                    if(comment.getLeft()>=left && comment.getRight() <= right){
                        comment.setStatus(0);
                    }
                    else if(comment.getLeft()<left && comment.getRight() > right){
                        comment.setRight(comment.getRight()-deletedNum);
                    }else if(comment.getLeft()>left && comment.getRight() > right){
                        comment.setLeft(comment.getLeft()-deletedNum);
                        comment.setRight(comment.getRight()-deletedNum);
                    }
                }).collect(Collectors.toList());
    }


}