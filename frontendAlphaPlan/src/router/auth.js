import RedirectView from "../views/RedirectView.vue"
import TestView from "../views/TestView.vue"

export const testView = (role) => {
  switch(role) {
    case 'ADMIN':
        return TestView;
    default:
        return RedirectView;
  }
}
