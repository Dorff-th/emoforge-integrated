import { NavLink } from "react-router-dom";

export default function AdminSidebar() {
  const menuItems = [
    { name: "대시보드", path: "/admin/dashboard" },
    { name: "회원관리", path: "/admin/members" },
    { name: "게시판카테고리관리", path: "/admin/posts/categories" },
    { name: "게시판관리", path: "/admin/posts" },
  ];

  return (
    <aside className="w-56 bg-gray-800 text-white flex flex-col">
      <div className="p-4 text-lg font-bold border-b border-gray-700">
        관리자 메뉴
      </div>

      <nav className="flex-1 mt-2">
        {menuItems.map((item) => (
          <NavLink
            key={item.path}
            to={item.path}
            className={({ isActive }) =>
              `block px-4 py-2 rounded-md mx-2 my-1 transition ${
                isActive
                  ? "bg-gray-700 font-semibold"
                  : "hover:bg-gray-700 hover:font-medium"
              }`
            }
          >
            {item.name}
          </NavLink>
        ))}
      </nav>
    </aside>
  );
}
