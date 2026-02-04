import { useParams } from "react-router-dom";

const PostDetailPage = () => {
  const params = useParams();

  console.log("PostDetail params:", params);

  return <div>Post Detail</div>;
};

export default PostDetailPage;
