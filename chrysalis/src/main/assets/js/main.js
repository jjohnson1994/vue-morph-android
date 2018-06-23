// An array of components uids mapped to their classes
// e.g. [{ uid: 5, component: {...} }]
let uidToVueComponentMap = [];

/**
 * Components
 * DOM elements representing Native Widgets
 */
Vue.component('scrollview', {
  template: '<div><slot></slot></div>',
  props: {
    styles: {
      type: Object,
      required: false,
      default: () => {},
    },
  },
  methods: {
    describe: function() {
      return {
        uid: this._uid,
        parent: this.$parent._uid,
        ...this.styles,
      };
    },
  },
  updated: function() {
    Android.onAppUpdate(JSON.stringify(this.describe()));
  },
});

Vue.component('linearlayout', {
  template: '<div><slot></slot></div>',
  props: {
    styles: {
      type: Object,
      required: false,
      default: () => {},
    },
  },
  methods: {
    describe: function() {
      return {
        uid: this._uid,
        parent: this.$parent._uid,
        ...this.styles,
      };
    },
  },
  updated: function() {
    Android.onAppUpdate(JSON.stringify(this.describe()));
  },
});

Vue.component('textview', {
  template: '<p>{{ text }}</p>',
  props: {
    text: {
      type: String|Number,
      required: false,
      default: '',
    },
    styles: {
      type: Object,
      required: false,
      default: () => {},
    },
  },
  updated: function() {
    Android.onAppUpdate(JSON.stringify(this.describe()));
  },
  methods: {
    describe: function() {
      return {
        uid: this._uid,
        parent: this.$parent._uid,
        text: this.text,
        ...this.styles,
      };
    },
  }
});

Vue.component('btn', {
  template: '<button>{{ text }}</button>',
  props: {
    text: {
      type: String,
      required: false,
      default: '',
    },
    styles: {
      type: Object,
      required: false,
      default: () => {},
    },
  },
  methods: {
    onClick: function() {
      Android.showToast('OnClick, Called Natively');
    },
    describe: function() {
      return {
        uid: this._uid,
        parent: this.$parent._uid,
        text: this.text,
        ...this.styles,
      };
    },
  },
  updated: function() {
    Android.onAppUpdate(JSON.stringify(this.describe()));
  },
});

Vue.component('checkbox', {
  template: '<label><input type="checkbox" :value="checked"/>{{ text }}</label>',
  props: {
    text: {
      required: false,
      default: '',
    },
    styles: {
      type: Object,
      required: false,
      default: () => {},
    },
  },
  data: function() {
    return {
      checked: this.value,
    };
  },
  methods: {
    setValue(checked) {
      console.log('is checked', checked, this.value);
      this.checked = checked;
      this.$emit('input', checked);
    },
    describe: function() {
      return {
        uid: this._uid,
        parent: this.$parent._uid,
        checked: this.checked,
        text: this.text,
        ...this.styles,
      };
    },
  },
  updated: function() {
    console.log("Checkbox, Passing Description", this.describe());
    Android.onAppUpdate(JSON.stringify(this.describe()));
  },
});

Vue.component('edittext', {
  template: '<input :value="text"/>',
  props: {
    styles: {
      type: Object,
      required: false,
      default: () => {},
    },
  },
  data: function() {
    return {
      text: this.value,
    };
  },
  methods: {
    setValue(text) {
      this.text = text;
      this.$emit('input', text);
    },
    describe: function() {
      return {
        uid: this._uid,
        parent: this.$parent._uid,
        text: this.text,
        ...this.styles,
      };
    },
  },
  updated: function() {
    Android.onAppUpdate(JSON.stringify(this.describe()));
  },
});

/**
 * Build a JSON object of the DOM
 * @param {VNode} root - Parse form here and down
 */
function buildVNodeTree(root) {
  console.log('parse: ', root);

  const styles = root.styles;
  const text = root.text;
  const value = root.value;
  const children = root.$children;

  let tag;
  let uid = root._uid;
  let description;

  if(root.$vnode) {
    tag = root.$vnode.componentOptions.tag;
    description = root.$vnode.componentInstance.describe();
  } else if (root._vnode) {
    tag = root._vnode.componentOptions.tag;
    description = root._vnode.componentInstance.describe();
  }

  uidToVueComponentMap.push({
    uid,
    component: root,
  });

  return {
    tag,
    ...description,
    children: children ? children.map(c => ({ ...this.buildVNodeTree(c) })) : [],
  };
}

/**
 * Event Dispatchers,
 * There functions allow DOM/ Vue Component Events to be called Natively
*/
function dispatchOnClick(uid) {
  const map = uidToVueComponentMap.filter(map => {
    return map.uid == uid;
  })[0];

  map.component.$vnode.data.on.click();
}

function dispatchOnChange(params) {
  const uid = params.split(', ')[0];
  const value = params.split(', ')[1];
  const map = uidToVueComponentMap.filter(map => {
    return map.uid == uid;
  })[0];

  map.component.setValue(value);
}

/**
 * Test Calculator App
 */
var app = new Vue({
  el: '#app',
  data: {
    message: 'Chrysalis!',
    total: 0,
    numberEntered: 0,
    totalsHistory: [],
    checkboxVal: false,
  },
  created: function() {
    window.setTimeout(() => {
      Android.created(JSON.stringify(buildVNodeTree(app)));
    }, 500);
  },
  updated: function() {
  },
  methods: {
    plus: function() {
      this.totalsHistory.unshift(this.total);
      this.total = this.total + parseInt(this.numberEntered);
    },
    subtract: function() {
      this.totalsHistory.unshift(this.total);
      this.total = this.total - parseInt(this.numberEntered);
    },
    multiply: function() {
      this.totalsHistory.unshift(this.total);
      this.total = this.total * parseInt(this.numberEntered);
    },
    divide: function() {
      this.totalsHistory.unshift(this.total);
      this.total = this.total / parseInt(this.numberEntered);
    },
    sayHello: function(message) {
      Android.showToast(message);
    }
  },
});
