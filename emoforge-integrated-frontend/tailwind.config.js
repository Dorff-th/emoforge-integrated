/** @type {import('tailwindcss').Config} */
export default {
  darkMode: "class", // 🔥 이 한 줄이 핵심

  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],

  theme: {
    extend: {
      borderRadius: {
        lg: "0.75rem",
        xl: "1rem",
      },
    },
  },

  plugins: [],
};
