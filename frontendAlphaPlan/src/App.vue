<script setup>
import routeViews from './views'
import { RouterView } from 'vue-router';

</script>
<template>
  <main class= "flex flex-row">
      <RouterView :name="routeViews.SIDEBAR"/>
      <RouterView :name="routeViews.MAIN" class="h-screen"/>
  </main>
</template>

<script>
import routeViews from './views'
import { RouterView } from 'vue-router';

export default {
  setup() {
    return {
      routeViews
    };
  }
};
</script>
