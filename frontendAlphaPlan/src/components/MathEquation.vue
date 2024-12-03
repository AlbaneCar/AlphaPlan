<template>
    <div ref="mathContainer"></div>
</template>

<script>
import katex from 'katex';
import 'katex/dist/katex.min.css';

export default {
props: {
    equation: {
    type: String,
    required: true
    }
},
watch: {
    equation: 'renderMath'
},
mounted() {
    this.renderMath();
},
methods: {
    renderMath() {
    katex.render(this.equation, this.$refs.mathContainer, {
        throwOnError: false
    });
    }
}
};
</script>
  