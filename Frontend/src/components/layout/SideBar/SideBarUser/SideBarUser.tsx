import icon0 from '../SideBarImgs/icon1.svg';
import './SideBarUser.css';

type SideBarUserProps = {
  urlImg: string;
  userName: string;
  userEmail: string;
};

export default function SideBarUser(props: SideBarUserProps) {
  return (
    <div className="user-container">
      <img className="user-picture" src={icon0} alt="" />
      <div className="user-text-container">
        <p className="user-name">{props.userName}</p>
        <p className="user-email">{props.userEmail}</p>
      </div>
    </div>
  );
}
