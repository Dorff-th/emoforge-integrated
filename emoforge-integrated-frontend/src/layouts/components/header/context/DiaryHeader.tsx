import { NavLink } from "react-router-dom";

export function DiaryHeader() {
  return (
    <nav className="flex w-full items-center justify-between">
      <div className="flex gap-4">
        <NavLink to="/user/diary/write">Write</NavLink>
        <NavLink to="/user/diary/calendar">Calendar</NavLink>
        <NavLink to="/user/diary/list">List</NavLink>
        <NavLink to="/user/diary/insights">Insights</NavLink>
      </div>

      <DiarySearchInput />
    </nav>
  );
}
