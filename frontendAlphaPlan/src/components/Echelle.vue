<template>
  <div class="relative max-w-full h-20 overflow-visible mx-auto px-6">
    <div class="absolute top-0 left-0 w-full text-center font-bold">
      {{ typeEchelle }}
    </div>
    <!-- Trait de l'échelle -->
    <div
      class="absolute top-1/2 h-2 bg-gradient-to-r from-blue-400 to-indigo-900 transform -translate-y-1/2"
      :style="{ width: `${((maxNote2 - minNote1) / maxNote) * 100}%`, left: `${(minNote1 / maxNote) * 100}%` }"
    ></div>
    <!-- Marqueurs -->
    <div
      v-for="(echelon, index) in echelons"
      :key="index"
      class="absolute"
      :style="{ left: `calc(${(echelon.note1 / maxNote) * 100}% - 12px)`, top: '50%' }"
    >
      <div class="relative w-6 h-6 px-4 bg-indigo-950 rounded-full flex items-center justify-center text-white text-xs font-bold transform -translate-y-1/2">
        {{ echelon.note1 }}
      </div>
    </div>
    <!-- Dernier marqueur -->
    <div
      class="absolute w-6 h-6 px-4 bg-indigo-950 rounded-full flex items-center justify-center text-white text-xs font-bold transform -translate-y-1/2"
      :style="{ left: `calc(${(maxNote2 / maxNote) * 100}% - 12px)`, top: '50%' }"
    >
      {{ maxNote2 }}
    </div>
    <!-- Commentaires -->
    <template v-for="(echelon, index) in echelons" :key="'comment-' + index">
      <div
        v-if="index < echelons.length - 1"
        class="absolute text-center"
        :style="{ left: `calc(${((echelons[index].note1 + echelons[index + 1].note1) / 2 / maxNote) * 100}% - 12px)`, top: 'calc(50% + 1.5rem)' }"
      >
        <div class="text-xs text-gray-700 whitespace-nowrap">
          {{ echelons[index].commentaire }}
        </div>
      </div>
    </template>
    <!-- Commentaire pour le dernier échelon -->
    <div
      v-if="echelons.length > 0"
      class="absolute text-center"
      :style="{ left: `calc(${((echelons[echelons.length - 1].note1 + maxNote2) / 2 / maxNote) * 100}% - 12px)`, top: 'calc(50% + 1.5rem)' }"
    >
      <div class="text-xs text-gray-700 whitespace-nowrap">
        {{ echelons[echelons.length - 1].commentaire }}
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'EchelleTrait',
  props: {
    echelons: {
      type: Array,
      required: true
    },
    typeEchelle: {
      type: String,
      required: true
    }
  },
  computed: {
    maxNote() {
      return Math.max(...this.echelons.map(echelon => Math.max(echelon.note1, echelon.note2)));
    },
    maxNote2() {
      return Math.max(...this.echelons.map(echelon => echelon.note2));
    },
    minNote1() {
      return Math.min(...this.echelons.map(echelon => echelon.note1));
    }
  }
};
</script>

<style scoped>
.relative {
  white-space: nowrap;
}

.absolute {
  white-space: nowrap;
}

.bg-gradient-to-r {
  left: 0;
  right: 0;
  margin-left: auto;
  margin-right: auto;
}
</style>
