export const getDaysInMonth = (year: number, month: number): string[] => {
  const date = new Date(year, month - 1, 1);
  const days: string[] = [];

  while (date.getMonth() === month - 1) {
    const yyyy = date.getFullYear();
    const mm = String(date.getMonth() + 1).padStart(2, '0');
    const dd = String(date.getDate()).padStart(2, '0');
    days.push(`${yyyy}-${mm}-${dd}`);
    date.setDate(date.getDate() + 1);
  }

  return days;
};
