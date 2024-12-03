<script setup>
import FolderDown from '../../assets/icons/FolderDown.vue';
import FolderUp from '../../assets/icons/FolderUp.vue';
import Edit from '../../assets/icons/Edit.vue';
import Save from '../../assets/icons/Save.vue';
import Validate from '../../assets/icons/Validate.vue'
import Loader from '../Loader/Loader.vue';
import ProgressSpinner from 'primevue/progressspinner';

</script>

<template>
    <button :class="['bg-custom-grey text-white font-CASlalomBold rounded-md duration-300']">
        <div class="flex justify-center items-center gap-2">      
            <ProgressSpinner style="width: 15px; height: 15px; color: red;" strokeWidth="5" fill="transparent" animationDuration="3s"/>
           <div class="leading-5">
                <slot />
           </div>
        </div>
    </button>
</template>

<style scoped>
:deep(.p-progress-spinner .p-progress-circle) {
    stroke: white !important;
}
</style>