# settings example in yaml
```ymal
  stargazer:
    url: redis://localhost:29181
    topic:
      - name: 't1'
        acl:
          white-list:
            - 'w1'
            - 'w2'
          black-list:
            - 'b1'
            - 'b2'
```
