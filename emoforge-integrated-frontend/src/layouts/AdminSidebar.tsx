import { useState } from "react";
import { NavLink, useLocation } from "react-router-dom";

type AdminMenuItem = {
  name: string;
  path?: string;
  basePath?: string;
  children?: AdminMenuItem[];
};

export default function AdminSidebar() {
  const location = useLocation();

  const menuItems: AdminMenuItem[] = [
    { name: "Dashboard", path: "/admin/dashboard" },
    { name: "Members", path: "/admin/members" },
    {
      name: "Post Management",
      basePath: "/admin/posts",
      children: [
        { name: "Posts", path: "/admin/posts" },
        { name: "Categories", path: "/admin/posts/categories" },
        { name: "Comments", path: "/admin/posts/comments" },
      ],
    },
  ];

  const [openMenus, setOpenMenus] = useState<Record<string, boolean>>(() =>
    menuItems.reduce<Record<string, boolean>>((acc, item) => {
      if (item.children) {
        acc[item.name] = Boolean(
          item.basePath && location.pathname.startsWith(item.basePath),
        );
      }

      return acc;
    }, {}),
  );

  const toggleMenu = (menuName: string) => {
    setOpenMenus((current) => ({
      ...current,
      [menuName]: !current[menuName],
    }));
  };

  return (
    <aside className="flex w-56 flex-col bg-gray-800 text-white">
      <div className="border-b border-gray-700 p-4 text-lg font-bold">
        Admin Menu
      </div>

      <nav className="mt-2 flex-1">
        {menuItems.map((item) => {
          if (!item.children) {
            return (
              <NavLink
                key={item.path}
                to={item.path ?? "/admin"}
                end
                className={({ isActive }) =>
                  `mx-2 my-1 block rounded-md px-4 py-2 transition ${
                    isActive ? "bg-gray-700 font-semibold" : "hover:bg-gray-700"
                  }`
                }
              >
                {item.name}
              </NavLink>
            );
          }

          const isParentActive = Boolean(
            item.basePath && location.pathname.startsWith(item.basePath),
          );

          return (
            <div key={item.name} className="mx-2 my-2">
              <button
                type="button"
                onClick={() => toggleMenu(item.name)}
                className={`flex w-full items-center justify-between rounded-md px-4 py-2 text-left font-semibold transition ${
                  isParentActive
                    ? "bg-gray-700 text-white"
                    : "text-gray-300 hover:bg-gray-700"
                }`}
              >
                <span>{item.name}</span>
                <span>{openMenus[item.name] ? "v" : ">"}</span>
              </button>

              {openMenus[item.name] && (
                <div className="ml-4">
                  {item.children.map((child) => (
                    <NavLink
                      key={child.path}
                      to={child.path ?? "/admin"}
                      end
                      className={({ isActive }) =>
                        `my-1 block rounded-md px-4 py-2 transition ${
                          isActive
                            ? "bg-gray-700 font-semibold"
                            : "hover:bg-gray-700"
                        }`
                      }
                    >
                      {child.name}
                    </NavLink>
                  ))}
                </div>
              )}
            </div>
          );
        })}
      </nav>
    </aside>
  );
}
