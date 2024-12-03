/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        custom: {
          lightblueAP: '#1A78D1',
          darkblueAP: '#1B1336',
          sideBarBG: '#ECF2F6',
          sideBarBackground : '#1B1336',
          sideBarLine: '#9196A9',
          sideBarTitles: '#9196A9',
          sideBarActiveBG: '#412d82',
          red: "#EE4836",
          grey: "#888888",
        }
      },
      backgroundImage: (theme) => ({
        'gradient-primary': `linear-gradient(to right, ${theme('colors.lblue')}, ${theme('colors.blue')})`,
      }),
      boxShadow: {
        'customShadow': '0px 4px 6px 0px rgb(48, 163, 240)',
      }
    },
    fontFamily: {
      CASlalomBold: ['CASlalomBold', 'sans-serif'],
      CASlalomRegular : ['CASlalomRegular', 'sans-serif'],
      CASlalomMedium : ['CASlalomExtendedM', "sans-serif"],
      Roboto : ['Roboto', "sans-serif"]
    },
  },
  plugins: [],
}

